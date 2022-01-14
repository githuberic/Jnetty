package com.lgq.netty.cases.chapter7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lgq
 */
@ChannelHandler.Sharable
public class SharableClientHandlerV2 extends ChannelInboundHandlerAdapter {
    AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf firstMessage = Unpooled.buffer(NoThreadSecurityClient.MSG_SIZE);
        for (int i = 0; i < firstMessage.capacity(); i++) {
            firstMessage.writeByte((byte) i);
        }
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf req = (ByteBuf) msg;
        if (counter.getAndIncrement() < 10000) {
            ctx.write(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
