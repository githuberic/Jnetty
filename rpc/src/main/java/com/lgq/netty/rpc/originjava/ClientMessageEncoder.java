package com.lgq.netty.rpc.originjava;

import com.lgq.netty.rpc.InputParam;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ObjectOutputStream;

/**
 * @author lgq
 */
public class ClientMessageEncoder extends MessageToByteEncoder<InputParam> {
    @Override
    protected void encode(ChannelHandlerContext ctx, InputParam msg, ByteBuf out) throws Exception {
        ObjectOutputStream o = new ObjectOutputStream(new ByteBufOutputStream(out));
        o.writeObject(msg);
    }
}
