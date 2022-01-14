package com.lgq.netty.codec.custom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lgq
 */
public class ServerMessageHandler extends ChannelInboundHandlerAdapter {
    private int messageCount = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = (String) msg;
        System.out.println("[" + (++messageCount) + "]接收到消息：" + content);

        // 注意：业务异常需要处理，不能不管，否则会调用exceptionCaught()
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
