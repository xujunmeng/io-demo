package com.junmeng.fixedlength.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class FixedLengthClientHandler   extends SimpleChannelInboundHandler {

    private static final Logger logger = Logger.getLogger(FixedLengthClientHandler.class.getName());

    //计数器
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        //获取服务端返回的数据，并打印到控制台
        String content = (String)msg;
        System.out.println("received from server : " + content + " counter :  " + counter.addAndGet(1));

    }

    //发生异常，关闭链路
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}

