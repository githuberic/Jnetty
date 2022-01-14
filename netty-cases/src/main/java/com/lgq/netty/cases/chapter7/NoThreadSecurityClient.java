package com.lgq.netty.cases.chapter7;

import com.lgq.netty.cases.chapter8.IotCarsClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lgq
 */
public class NoThreadSecurityClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8087"));
    static final int MSG_SIZE = 256;


    public void run() throws Exception {
        connect();
    }

    public void connect() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(8);
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new IotCarsClientHandler());
                    }
                });
        ChannelFuture cf = null;
        for (int i = 0; i < 8; i++) {
            cf = b.connect(HOST, PORT).sync();
        }
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        new NoThreadSecurityClient().run();
    }
}
