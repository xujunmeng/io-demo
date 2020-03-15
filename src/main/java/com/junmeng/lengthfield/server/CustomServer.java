package com.junmeng.lengthfield.server;

import com.junmeng.lengthfield.CustomV1Decoder;
import com.junmeng.lengthfield.CustomV1Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomServer {
    public static void main(String[] args) {
        int port = 8012;
        //绑定服务端口，并启动netty服务端
        new CustomServer().bind(port);

    }

    public void bind(int port) {
        //创建两个EventLoopGroup实例
        //EventLoopGroup是包含一组专门用于处理网络事件的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端辅助启动类ServerBootstrap对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //设置NIO线程组
            serverBootstrap
                    .group(bossGroup, workerGroup)

                    //设置NioServerSocketChannel，对应于JDK NIO类ServerSocketChannel
                    .channel(NioServerSocketChannel.class)

                    //设置TCP参数，连接请求的最大队列长度
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    //设置I/O时间处理类，用来处理消息的编解码及我们的业务逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //解决粘包半包，根据消息长度自动拆包
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));

                            //配置自定义序列化解码工具
                            socketChannel.pipeline().addLast(new CustomV1Decoder());

                            //解决粘包半包问题，附加消息长度在消息头部
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(2));

                            //配置自定义序列化编码工具
                            socketChannel.pipeline().addLast(new CustomV1Encoder());

                            socketChannel.pipeline().addLast(new CustomServerHandler());

                        }
                    });

            //绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}


