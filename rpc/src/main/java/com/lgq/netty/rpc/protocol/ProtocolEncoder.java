package com.lgq.netty.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lgq
 */
public class ProtocolEncoder extends MessageToByteEncoder<TcpPackageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          TcpPackageProtocol msg,
                          ByteBuf out) throws Exception {
        // 1.写入消息的开头的信息标志(int类型)
        out.writeInt(TcpPackageProtocolHead.head);
        // 2.写入消息的长度(int 类型)
        out.writeInt(msg.getMessageLength());
        // 3.写入消息的内容(byte[]类型)
        out.writeBytes(msg.getMessage());
    }
}
