package com.junmeng.delimiterbased.client;

import com.junmeng.delimiterbased.Consts;
import com.junmeng.netty.client.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by jgsoft on 2020/3/14.
 */
public class DelimiterBaseClient {

    public static void main(String[] args) {
        int port = 8033;
        String host = "127.0.0.1";
        new DelimiterBaseClient().connect(port, host);
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

                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                            //设置DelimiterBasedFrameDecode处理器
                            ByteBuf delimiter = Unpooled.copiedBuffer(Consts.delimiter_tag.getBytes());
                            nioSocketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));

                            //设置StringDecoder处理器
                            nioSocketChannel.pipeline().addLast(new StringDecoder());

                            //配置客户单处理网络I/O事件的类
                            nioSocketChannel.pipeline().addLast(new DelimiterBaseClientHandler());
                        }
                    });

            //发起异步连接操作
            ChannelFuture f = bootstrap.connect(host, port).sync();

            for (int i = 0; i < 1000; i++) {

                //构造客户端发送的数据bytebuf对象
                String content = "你好，Netty!!!!!!!!!!!!#" + Consts.delimiter_tag;
                byte[] req = content.getBytes();
                ByteBuf messageBuffer = Unpooled.buffer(req.length);
                messageBuffer.writeBytes(req);

                //向服务端发送数据
                ChannelFuture channelFuture = f.channel().writeAndFlush(messageBuffer);
                channelFuture.syncUninterruptibly();

            }

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
