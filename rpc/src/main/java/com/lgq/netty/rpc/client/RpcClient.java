package com.lgq.netty.rpc.client;

import com.lgq.netty.rpc.InputParam;
import com.lgq.netty.rpc.protocol.ClientMessageDecoder;
import com.lgq.netty.rpc.protocol.ClientMessageEncoder;
import com.lgq.netty.rpc.protocol.ProtocolDecoder;
import com.lgq.netty.rpc.protocol.ProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lgq
 */
public class RpcClient {
    int port;
    String host;

    public RpcClient(int port, String host) {
        this.port = port;
        this.host = host;
    }


    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ProtocolDecoder())
                                    .addLast(new ProtocolEncoder())

                                    //第一种java原生的序列化方式
                                    /*
                                    .addLast("decoder", new ClientMessageDecoder())
                                    .addLast("encoder", new ClientMessageEncoder())
                                    */

                                    //第二种netty支持的messagePack序列化方式
                                    .addLast("decoder", new ClientMessageDecoder())
                                    .addLast("encoder", new ClientMessageEncoder())

                                    //第三种添加了tcp自定义协议的序列化方式
                                    /*
                                    .addLast(new ClientMessageDecoder())
                                    .addLast(new ClientMessageEncoder())
                                    */
                                    .addLast(new RpcClientHandler());

                        }
                    });

            Channel channel = bootstrap.connect(host, port).sync().channel();
            for (int i = 0; i < 100; i++) {
                InputParam inputParam = new InputParam();
                inputParam.setNum1(i);
                inputParam.setNum2(i * 2);
                channel.writeAndFlush(inputParam);
                System.out.println("client 发送出去的信息是" + inputParam.toString());
            }

            //从键盘读出一个字符，然后返回它的Unicode码;目的是等待client接收完消息再退出
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new RpcClient(8080, "127.0.0.1").run();
    }
}
