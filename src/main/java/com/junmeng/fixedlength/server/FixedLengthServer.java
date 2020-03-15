package com.junmeng.fixedlength.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class FixedLengthServer {

    public static void main(String[] args) {
        int port = 8022;
        //绑定服务端口，并启动netty服务端
        new FixedLengthServer().bind(port);

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
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                            //设置FixedLengthFrameDecoder处理器，13为所需要传输的字符串的字节数组长度
                            nioSocketChannel.pipeline().addLast(new FixedLengthFrameDecoder(13));

                            //设置StringDecoder处理器
                            nioSocketChannel.pipeline().addLast(new StringDecoder());

                            nioSocketChannel.pipeline().addLast(new FixedLengthServerHandler());
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
