package com.junmeng.objectdecoder.server;

import com.junmeng.objectdecoder.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class JavaSerializeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收客户端发送的字符串，其中经过前面解码工具objectdecoder的处理，
        //将字节码消息自动转换成UserInfo对象
        UserInfo req = (UserInfo)msg;
        System.out.println("received from client : " + req.toString());

    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
