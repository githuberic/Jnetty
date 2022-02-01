package com.lgq.jnetty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author lgq
 */
public class Byte2IntegerReplayDecoderE extends ReplayingDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) {
        int i = in.readInt();
        System.out.println("解码出一个整数: " + i);
        out.add(i);
    }
}
