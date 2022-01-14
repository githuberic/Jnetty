package com.lgq.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lgq
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> typeClass;

    public RpcDecoder(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> decoded) {
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Object obj = SerializingUtil.deserialize(bytes, typeClass);

        decoded.add(obj);
    }
}
