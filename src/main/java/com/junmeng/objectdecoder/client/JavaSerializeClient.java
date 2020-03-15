package com.junmeng.objectdecoder.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class JavaSerializeClient {
    public static void main(String[] args) {
        int port = 8011;
        String host = "127.0.0.1";
        new JavaSerializeClient().connect(port, host);
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

                            //设置java序列化解码工具objectencoder
                            socketChannel.pipeline().addLast(new ObjectEncoder());

                            //配置客户单处理网络I/O事件的类
                            socketChannel.pipeline().addLast(new JavaSerializeClientHandler());
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