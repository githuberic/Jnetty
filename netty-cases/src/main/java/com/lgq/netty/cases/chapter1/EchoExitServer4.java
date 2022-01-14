package com.lgq.netty.cases.chapter1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Logger;

/**
 * @author lgq
 */
public class EchoExitServer4 {
    static Logger logger = Logger.getLogger(EchoExitServer2.class.getName());

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 网络IO读写的工作线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });
            // 同步方式监听
            ChannelFuture cf = b.bind(18080).sync();

            // Close future 监听channel关闭
            cf.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> {
                // 链路关闭时再释放线程池和连接句柄
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();

                logger.info(channelFuture.channel().toString() + "Channel close");
            });
        } catch (Exception ex) {
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }
}
