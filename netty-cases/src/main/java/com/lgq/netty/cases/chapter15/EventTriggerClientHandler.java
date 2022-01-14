package com.lgq.netty.cases.chapter15;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lgq
 */
public class EventTriggerClientHandler extends ChannelInboundHandlerAdapter {
    private static AtomicInteger SEQ = new AtomicInteger(0);

    static final String ECHO_REQ = "Hi,welcome to Netty ";

    static final String DELIMITER = "$_";

    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            int counter = SEQ.incrementAndGet();
            if (counter % 10 == 0) {
                ctx.writeAndFlush(Unpooled.copiedBuffer((ECHO_REQ + DELIMITER).getBytes()));
            } else
                ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
