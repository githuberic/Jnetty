package com.lgq.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lgq
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> typeClass;

    public RpcEncoder(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) {
        if (typeClass.isInstance(o)) {
            byte[] data = SerializingUtil.serialize(o);
            byteBuf.writeBytes(data);
        }
    }
}
