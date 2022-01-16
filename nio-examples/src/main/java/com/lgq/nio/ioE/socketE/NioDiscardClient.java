package com.lgq.nio.ioE.socketE;

import com.lgq.nio.common.util.NioEConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lgq
 */
public class NioDiscardClient {
    
    public static void main(String[] args) throws IOException {
        startClient();
    }
    
    /**
     * 客户端
     */
    public static void startClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress(NioEConfig.SOCKET_SERVER_IP, NioEConfig.SOCKET_SERVER_PORT);

        // 1、获取通道（channel）
        SocketChannel socketChannel = SocketChannel.open(address);
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {
        }

        System.out.println("客户端连接成功");

        // 3、分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world".getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
}
