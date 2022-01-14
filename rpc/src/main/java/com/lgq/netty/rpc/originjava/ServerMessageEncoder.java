package com.lgq.netty.rpc.originjava;

import com.lgq.netty.rpc.OutputParam;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ObjectOutputStream;

/**
 * @author lgq
 */
public class ServerMessageEncoder extends MessageToByteEncoder<OutputParam> {
    @Override
    protected void encode(ChannelHandlerContext ctx, OutputParam msg, ByteBuf out) throws Exception {
        ObjectOutputStream o = new ObjectOutputStream(new ByteBufOutputStream(out));
        o.writeObject(msg);
    }
}
