package com.lgq.netty.cases.chapter16;

import io.netty.buffer.ByteBuf;
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
public class TrafficShappingClientHandler extends ChannelInboundHandlerAdapter {
    private static AtomicInteger SEQ = new AtomicInteger(0);

    static final byte[] ECHO_REQ = new byte[1024 * 1024];

    static final String DELIMITER = "$_";

    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ByteBuf buf = null;
            for (int i = 0; i < 10; i++) {
                buf = Unpooled.copiedBuffer(ECHO_REQ, DELIMITER.getBytes());
                SEQ.getAndAdd(buf.readableBytes());
                if (ctx.channel().isWritable()) {
                    ctx.write(buf);
                }
            }
            ctx.flush();
            int counter = SEQ.getAndSet(0);
            System.out.println("The client send rate is : " + counter + " bytes/s");
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
