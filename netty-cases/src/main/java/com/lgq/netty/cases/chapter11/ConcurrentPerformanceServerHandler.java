package com.lgq.netty.cases.chapter11;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lgq
 */
public class ConcurrentPerformanceServerHandler extends ChannelInboundHandlerAdapter {
    AtomicInteger counter = new AtomicInteger(0);
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(() ->
        {
            int qps = counter.getAndSet(0);
            System.out.println("The server QPS is : " + qps);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ((ByteBuf) msg).release();
        counter.incrementAndGet();
        Random random = new Random();
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
