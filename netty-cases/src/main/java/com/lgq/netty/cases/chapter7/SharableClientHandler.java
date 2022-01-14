package com.lgq.netty.cases.chapter7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lgq
 */
@ChannelHandler.Sharable
public class SharableClientHandler extends ChannelInboundHandlerAdapter {
    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf req = (ByteBuf) msg;
        if (counter++ < 10000) {
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
