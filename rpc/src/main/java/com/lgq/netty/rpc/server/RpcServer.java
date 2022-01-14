package com.lgq.netty.rpc.server;

import com.lgq.netty.rpc.protocol.ProtocolDecoder;
import com.lgq.netty.rpc.protocol.ProtocolEncoder;
import com.lgq.netty.rpc.protocol.ServerMessageDecoder;
import com.lgq.netty.rpc.protocol.ServerMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lgq
 */
public class RpcServer {
    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ProtocolDecoder())
                                    .addLast(new ProtocolEncoder())
                                    // 第一种java原生的序列化方式
                                    /*
                                    .addLast("decoder", new ServerMessageDecoder())
                                    .addLast("encoder", new ServerMessageEncoder())
                                    */

                                    // 第二种netty支持的messagePack序列化方式
                                    .addLast("decoder", new ServerMessageDecoder())
                                    .addLast("encoder", new ServerMessageEncoder())

                                    /*第三种添加了tcp自定义协议的序列化方式
                                    .addLast(new ServerMessageDecoder())
                                    .addLast(new ServerMessageEncoder())
                                     */

                                    .addLast(new RpcServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("RpcServer error:" + e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            System.out.println("RpcServer 关闭了");
        }
    }

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new RpcServer(port).run();
    }
}
