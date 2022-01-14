package com.lgq.netty.cases.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lgq
 * 1：继承SimpleChannelInboundHandler 资源自动释放
 */
public class RouterServerHandlerV2 extends SimpleChannelInboundHandler<ByteBuf> {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] body = new byte[msg.readableBytes()];
        executorService.execute(() ->
        {
            if (allocator == null) {
                allocator = new PooledByteBufAllocator(true);
            }
            //解析请求消息，做路由转发，代码省略...
            //转发成功，返回响应给客户端
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
