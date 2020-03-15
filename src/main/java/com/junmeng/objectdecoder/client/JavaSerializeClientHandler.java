package com.junmeng.objectdecoder.client;

import com.junmeng.objectdecoder.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class JavaSerializeClientHandler    extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(JavaSerializeClientHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //channel建立之后，向服务端发送消息，需要注意的是这里写入的消息是完整的userinfo对象
        //因为后续会被objectencoder进行编码处理
        UserInfo userInfo = new UserInfo();
        userInfo.setName("asdf");
        userInfo.setUserId(1);
        userInfo.setEmail("asdf@163.com");
        userInfo.setMobile("123");
        userInfo.setRemark("aaaa");

        ctx.writeAndFlush(userInfo);
    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}


