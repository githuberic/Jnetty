package com.lgq.jnetty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lgq
 */
public class Integer2ByteEncoderE extends MessageToByteEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Integer integer,
                          ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(integer);
        System.out.println("encoder Integer = " + integer);
    }
}
