package com.lgq.jnetty.cases.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lgq
 */
public class ConcurrentPerformanceServerHandlerV2 extends ChannelInboundHandlerAdapter {
    static AtomicInteger counter = new AtomicInteger(0);
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public ConcurrentPerformanceServerHandlerV2() {
        scheduledExecutorService.scheduleAtFixedRate(() ->
        {
            int qps = counter.getAndSet(0);
            System.out.println("The server QPS is : " + qps);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ((ByteBuf) msg).release();
        executorService.execute(() ->
        {
            counter.incrementAndGet();
            Random random = new Random();
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == SslHandshakeCompletionEvent.SUCCESS) {
            //Ö´ÐÐÁ÷¿ØÂß¼­
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
