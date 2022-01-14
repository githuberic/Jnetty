package com.lgq.netty.rpc.messagepack;

import com.lgq.netty.rpc.InputParam;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author lgq
 */
public class ServerMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //获取要解码的byte数组
        int length = msg.readableBytes();
        byte[] array = new byte[length];
        //调用MessagePack 的read方法将其反序列化为Object对象
        msg.getBytes(msg.readerIndex(), array, 0, length);

        MessagePack msgpack = new MessagePack();
        InputParam inputParam = msgpack.read(array, InputParam.class);

        out.add(inputParam);
    }
}
