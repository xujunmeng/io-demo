package com.junmeng.lengthfield.client;

import com.junmeng.lengthfield.CustomV1Decoder;
import com.junmeng.lengthfield.CustomV1Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomClient {
    public static void main(String[] args) {
        int port = 8012;
        String host = "127.0.0.1";
        new CustomClient().connect(port, host);
    }

    public void connect(int port, String host) {

        //创建客户端处理I/O读写的NIO线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //创建客户端服务启动类
            Bootstrap bootstrap = new Bootstrap();
            //设置NIO线程组
            bootstrap.group(eventLoopGroup)

                    //设置NIOSocketChannel,对应于JDK NIO类socketchannel类
                    .channel(NioSocketChannel.class)

                    //设置TCP参数
                    .option(ChannelOption.TCP_NODELAY, true)

                    .handler(new ChannelInitializer<SocketChannel>() {
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
                            socketChannel.pipeline().addLast(new CustomClientHandler());
                        }
                    });

            //发起异步连接操作
            ChannelFuture f = bootstrap.connect(host, port).sync();

            //等待客户端链路关系
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放NIO线程组
            eventLoopGroup.shutdownGracefully();
        }
    }

}
