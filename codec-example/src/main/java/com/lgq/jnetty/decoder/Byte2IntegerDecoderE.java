package com.lgq.jnetty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lgq
 */
public class Byte2IntegerDecoderE extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 长度判定
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            System.out.println("解码出一个整数: " + i);
            out.add(i);
        }
    }
}
