package com.lgq.netty.cases.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author lgq
 */
public class LoadRunnerClientHandler extends ChannelInboundHandlerAdapter{
    private final ByteBuf firstMessage;

    Runnable loadRunner;

    AtomicLong sendSum = new AtomicLong(0);

    Runnable profileMonitor;

    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    /**
     * Creates a client-side handler.
     */
    public LoadRunnerClientHandler() {
        firstMessage = Unpooled.buffer(SIZE);
        for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        loadRunner = () -> {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ByteBuf msg = null;
            final int len = "Netty OOM Example".getBytes().length;
            while(true)
            {
                msg = Unpooled.wrappedBuffer("Netty OOM Example".getBytes());
                ctx.writeAndFlush(msg);
            }
        };
        new Thread(loadRunner, "LoadRunner-Thread").start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

