package com.lgq.netty.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lgq
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
    public final int BASE_LENGTH = 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 可读长度必须大于基本长度
        if (in.readableBytes() >= BASE_LENGTH) {
            // 记录包头开始的index
            int beginReader;

            while (true) {
                // 获取包头开始的index
                beginReader = in.readerIndex();

                // 标记包头开始的index
                in.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                if (in.readInt() == TcpPackageProtocolHead.head) {
                    break;
                }

                // 未读到包头，略过一个字节，每次略过，一个字节，去读取，包头信息的开始标记
                in.resetReaderIndex();
                in.readByte();

                // 当略过，一个字节之后，数据包的长度，又变得不满足
                // 此时，应该结束。等待后面的数据到达
                if (in.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            // 消息的长度

            int length = in.readInt();
            // 判断请求数据包数据是否到齐
            if (in.readableBytes() < length) {
                // 还原读指针
                in.readerIndex(beginReader);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            in.readBytes(data);

            TcpPackageProtocol protocol = new TcpPackageProtocol(data.length, data);
            out.add(protocol);
        }
    }
}
