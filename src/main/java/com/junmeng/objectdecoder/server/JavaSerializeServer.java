package com.junmeng.objectdecoder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class JavaSerializeServer {

    public static void main(String[] args) {
        int port = 8011;
        //绑定服务端口，并启动netty服务端
        new JavaSerializeServer().bind(port);

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

                            //设置java默认序列化解码工具objectdecoder
                            //添加对象解码器，负责对序列化pojo对象进行解码，设置对象序列化最大长度为1M，
                            //防止内存溢出
                            //设置线程安全的weakreferenceMap对类加载器进行缓存，支持多线程并发访问，防止内存溢出
                            socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                    ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));

                            socketChannel.pipeline().addLast(new JavaSerializeServerHandler());
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

