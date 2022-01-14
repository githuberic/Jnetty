package com.lgq.netty.rpc.originjava;

import com.lgq.netty.rpc.OutputParam;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author lgq
 */
public class ClientMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteBufInputStream(in));
        OutputParam outputParam = (OutputParam)inputStream.readObject();
        out.add(outputParam);
    }
}
