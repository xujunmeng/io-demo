package com.junmeng.lengthfield.server;

import com.junmeng.objectdecoder.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomServerHandler  extends ChannelInboundHandlerAdapter {

    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收客户端发送的字符串，其中经过前面解码工具处理，
        //将字节码消息自动转换成UserInfo对象
        UserInfo req = (UserInfo)msg;
        System.out.println("received from client : " + req.toString() + " counter : " + counter.incrementAndGet());

    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

