package com.junmeng.delimiterbased.server;

import com.junmeng.delimiterbased.Consts;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jgsoft on 2020/3/14.
 */
public class DelimiterBaseServerHandler  extends SimpleChannelInboundHandler {

    //计数器
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收客户端发送的字符串，并打印到控制台
        String content = (String)msg;
        System.out.println("received from client : " + content + " counter : " + counter.addAndGet(1));

        //加入分隔符，将数据重新发送到客户端
        content += Consts.delimiter_tag;
        ByteBuf echo = Unpooled.copiedBuffer(content.getBytes());
        ctx.writeAndFlush(echo);
    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //将发送缓冲区中的消息全部写入socketchannel中
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
