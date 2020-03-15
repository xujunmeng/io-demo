package com.junmeng.lengthfield;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomV1Encoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //使用hessian序列化对象
        byte[] data = HessianSerializer.serialize(msg);
        out.writeBytes(data);

    }
}
