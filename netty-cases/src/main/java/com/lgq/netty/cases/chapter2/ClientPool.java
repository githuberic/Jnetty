package com.lgq.netty.cases.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lgq
 * <p>
 * 1:创建链接池的正确方式 创建一个nioeventloopgroup(n个 nio线程) client 共享nioeventloop 连接到netty server
 * 2:netstat -an|find 'established'|find '18081'
 * 3:nioeventloopgroup2-1...8, 默认为cpu 2倍，和tcp连接数无关，解决了线程膨胀问题
 */
public class ClientPool {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "18081"));

    static void initClientPool(int poolSize) throws Exception {
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
        for (int i = 0; i < poolSize; i++) {
            b.connect(HOST, PORT).sync();

            /*
            以下是错误代码
            创建一个nioeventloopgroup(n个 nio线程) client 共享nioeventloop ，当channnel关闭时，不能销毁channel所使用的
            nioeventloop/eventloopgroup
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().closeFuture().addListener((r) -> {
                group.shutdownGracefully();
            });*/
        }
    }
}
