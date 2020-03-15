package com.junmeng.lengthfield;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class CustomV1Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int dataLength = in.readableBytes();
        if (dataLength <= 0) {
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        //将字节数组使用Hessian反序列化为对象
        Object obj = HessianSerializer.deserialize(data);
        out.add(obj);
    }
}
