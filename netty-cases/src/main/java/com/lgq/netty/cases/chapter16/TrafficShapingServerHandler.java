package com.lgq.netty.cases.chapter16;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Sharable
public class TrafficShapingServerHandler extends ChannelInboundHandlerAdapter {
    AtomicInteger counter = new AtomicInteger(0);

    static ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    public TrafficShapingServerHandler() {
        es.scheduleAtFixedRate(() -> {
            System.out.println("The server receive client rate is : " + counter.getAndSet(0) + " bytes/s");
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        counter.addAndGet(body.getBytes().length);
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
