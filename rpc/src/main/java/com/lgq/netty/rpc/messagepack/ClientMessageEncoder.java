package com.lgq.netty.rpc.messagepack;

import com.lgq.netty.rpc.InputParam;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author lgq
 */
public class ClientMessageEncoder extends MessageToByteEncoder<InputParam> {
    @Override
    protected void encode(ChannelHandlerContext ctx, InputParam msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();
        out.writeBytes(msgpack.write(msg));
    }
}
