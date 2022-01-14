# 客户端连接池资源

## Java NIO 客户端创建原理
1. 打开socketchannel

    ```java
    SocketChannel clientChannel = SockectChannel.open();
    ```

2. 设置socketchanel为非阻塞模式，同时设置tcp参数
    ```java
    clientChannel.configureBlocking(false);
    socket.setReuseAddress(true);
    socket.setReceiveBufferSize(BUFFER_SIZE);
    socket.setSendBufferSize(BUFFER_SIZE);
    ```
3. 异步连接服务器
    ```java
    boolean connected = clientChannel.connect(new InetSocketAddress("ip","port"));
    ```
4. 判断连接结果，如果连接成功 则跳转步骤10，否则执行步骤5
连接成功，则直接注册读状态位到多路复用器中，如果没有连接成功(异步连接返回false)，说明客户端已经发送sync 包，服务端没有返回ack 包，物理链路还没有建立
    ```java
    if (connected) {
        clientChannel.register(selector, SelectKey.OP_READ, ioHandler);
    }else {
        clientChannel.register(selector, SelectKey.OP_CONNECT, ioHandler);
    }
    ```
5. 向reactor线程的多路复用器注册op_connect事件，监听服务端的TCP_ACK应答
    ```java
    clientChannel.register(selector,SelectionKey.OP_CONNECT,ioHandler);
    ```
6. 创建Reactor线程，创建selector 启动线程
    ```java
    Selector selector = Selector.Open();
    New Thread(new ReactorTask()).start();
    ```
7. selector轮询就绪的key
    ```java
    int num = selector.select();
    Set selectKeys = selector.selectedKey();
    Iterator it = selectedKeys.iterator();
    while(it.hasNext()) {
        SelectionKey key = (SelectionKey)it.next();
        // 处理IO事件
    ```
8. handleconnect()
    ```java
    if (key.isConnectable()) {
        handlerConnect();
    }
    ```
9. 判断连接是否完成，如果完成则执行步骤10
    ```java
    if (channel.finishConnect()) {
        registerConnnect();
    }
    ```
10. 向多路复用器注册读事件op_read
    ```java
    clientChannel.register(selector,SelectionKey.OP_READ, ioHandler);
    ```
11. handleRead()异步读请求消息到bytebuffer
    ```java
    int readNumber = channel.read(receivedBuffer);
    ```
12. decode 请求消息
13. 异步写butebuffer到socketchannel
    ````java
    socketChannel.write(buffer);
    ````
    
## Bootstrap工具
Bootstrap 是netty提供的客户端连接工具类，主要用于简化客户端的创建；常用功能
- 设置eventloopgroup线程池 通常多个连接共享一个eventloopgroup，它的大小默认为CPU内核数的2倍；
- TCP参数设置,无论是NIO、BIO都需要tcp参数设置，比如 接收、发送缓冲区大小，连接超时时间，常用参数如下
    - SO_TIMEOUT 控制读取操作将阻塞多少ms
    - SO_SNDBUF 套接字使用的发送缓冲区大小
    - SO_RECBUF 套接字使用的接收缓冲区大小
    - SO_REUSEADDR 新的serversocket绑定到与旧的serversocket相同的端口上
    - CONNECT_TIMEOUT_MILLIS 客户端连接超时时间
    - TCP_NODELAY 激活或禁止tcp_nodelay选项，决定是否使用nagle算法；