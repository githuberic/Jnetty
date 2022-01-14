package com.lgq.netty.cases.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lgq
 *
 * 1:继承ChannelInboundHandlerAdapter 它的channelRead(ChannelHandlerContext ctx, Object msg)
 * 方法执行完毕后，channelhandler就结束了 但请求的 bytebuf没有被释放；
 */
public class RouterServerHandler extends ChannelInboundHandlerAdapter {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf reqMsg = (ByteBuf)msg;
        byte [] body = new byte[reqMsg.readableBytes()];
//        ReferenceCountUtil.release(reqMsg);
        executorService.execute(()->
        {
            // 解析请求消息，做路由转发，代码省略...
            // 转发成功，返回响应给客户端
            // 过一段时间 内存不断飙升，有内存泄露问题
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            //作为示例，简化处理，将请求返回
            respMsg.writeBytes(body);
            ctx.writeAndFlush(respMsg);
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
