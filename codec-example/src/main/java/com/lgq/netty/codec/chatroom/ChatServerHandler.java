package com.lgq.netty.codec.chatroom;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author lgq
 */
public class ChatServerHandler extends SimpleChannelInboundHandler {
    // Netty提供了ChannelGroup接口，该接口继承Set接口
    // 因此可以通过ChannelGroup可管理服务器端所有的连接的Channel，然后对所有的连接Channel广播消息。
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 每当服务端收到新的客户端连接时,客户端的channel存入ChannelGroup列表中,并通知列表中其他客户端channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取连接的channel
        Channel incomming = ctx.channel();
        //通知所有已经连接到服务器的客户端，有一个新的通道加入
        for (Channel channel : channelGroup) {
            channel.writeAndFlush("[系统消息]-" + incomming.remoteAddress() + "加入\n");
        }
        channelGroup.add(ctx.channel());
    }

    /**
     * 每当服务端断开客户端连接时,客户端的channel从ChannelGroup中移除,并通知列表中其他客户端channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //获取连接的channel
        Channel incomming = ctx.channel();
        for (Channel channel : channelGroup) {
            channel.writeAndFlush("[系统消息]-" + incomming.remoteAddress() + "离开\n");
        }
        //从服务端的channelGroup中移除当前离开的客户端
        channelGroup.remove(ctx.channel());
    }
    /**
     * 服务端监听到客户端活动
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //服务端接收到客户端上线通知
        Channel incoming = ctx.channel();
        System.out.println("ChatClient:" + incoming.remoteAddress() + "在线");
    }

    /**
     * 服务端监听到客户端不活动
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //服务端接收到客户端掉线通知
        Channel incoming = ctx.channel();
        System.out.println("ChatClient:" + incoming.remoteAddress() + "掉线");
    }

    /**
     * 每当从服务端读到客户端写入信息时,将信息转发给其他客户端的Channel.
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        Channel incomming = channelHandlerContext.channel();
        //将收到的信息转发给全部的客户端channel
        for (Channel channel : channelGroup) {
            if (channel != incomming) {
                channel.writeAndFlush("[" + incomming.remoteAddress() + "]" + o.toString() + "\n");
            } else {
                channel.writeAndFlush("[You]" + o.toString() + "\n");
            }
        }
    }
}
