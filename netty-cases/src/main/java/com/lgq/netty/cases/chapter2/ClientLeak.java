package com.lgq.netty.cases.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author lgq
 * 连接池资源泄露(内存溢出，线程膨胀)
 * 1：创建了大量的eventloopgroup线程池，其里面的NioEventloop线程 对应一个tcp连接(一个TCP连接对应一个NIO线程的模式)
 */
public class ClientLeak {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "18081"));

    public void main(String[] args) throws Exception {
        TimeUnit.SECONDS.sleep(30);
        initClientPool(100);
    }

    static void initClientPool(int poolSize) throws Exception {
        for (int i = 0; i < poolSize; i++) {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler());
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().closeFuture().addListener((r) -> {
                group.shutdownGracefully();
            });
        }
    }
}
