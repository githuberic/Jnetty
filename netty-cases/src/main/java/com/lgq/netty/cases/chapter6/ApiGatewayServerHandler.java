package com.lgq.netty.cases.chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author lgq
 */
public class ApiGatewayServerHandler extends ChannelInboundHandlerAdapter {
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    /**
     * api网关，每次收到请求，无论大小，都构造一个64k的char数组，用于处理和转发消息，如果后端处理慢
     * 则会导致任务队列挤压，每个任务都会持有一个64k array，挤压多了就会转移到老年代，一旦触发老年代GC耗时较长，就会导致
     * 系统吞吐降低为0
     *
     * @param ctx
     * @param msg
     */
    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);

        char[] req = new char[64 * 1024];
        executorService.execute(() ->
        {
            char[] dispatchReq = req;
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }*/

    /**
     * 1：按请求消息大小来初始化char数组
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
        char[] req = new char[((ByteBuf) msg).readableBytes()];
        executorService.execute(() ->
        {
            char[] dispatchReq = req;
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
