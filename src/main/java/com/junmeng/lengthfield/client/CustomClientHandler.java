package com.junmeng.lengthfield.client;

import com.junmeng.objectdecoder.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomClientHandler     extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(CustomClientHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 1000; i++) {
            //channel建立之后，向服务端发送消息，需要注意的是这里写入的消息是完整的userinfo对象
            UserInfo userInfo = new UserInfo();
            userInfo.setName("asdf");
            userInfo.setUserId(1);
            userInfo.setEmail("asdf@163.com");
            userInfo.setMobile("123");
            userInfo.setRemark("aaaa");

            ctx.writeAndFlush(userInfo);
        }

    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}


