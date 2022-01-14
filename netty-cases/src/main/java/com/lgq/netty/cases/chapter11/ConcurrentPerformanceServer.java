package com.lgq.netty.cases.chapter11;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author lgq
 */
public class ConcurrentPerformanceServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "18088"));
    static final EventExecutorGroup executor = new DefaultEventExecutorGroup(100);

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelHandler serviceHandler = new ConcurrentPerformanceServerHandlerV2();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.config().setAllocator(UnpooledByteBufAllocator.DEFAULT);
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(executor, serviceHandler);
                            //p.addLast(serviceHandler);
                        }
                    }).childOption(ChannelOption.SO_RCVBUF, 8 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 8 * 1024);
            ChannelFuture f = bootstrap.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
