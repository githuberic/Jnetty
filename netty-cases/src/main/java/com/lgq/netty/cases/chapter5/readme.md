# 发送队列解压导致内存泄露

### Netty 发送消息
Channel/write时，如果发送方为业务线程，则将发送操作封装成WriteTask,放到NioEventLoop中执行；
如果Netty IO线程 NioEventLoop无法完成多消息发送时，则发送队列会挤压，进而导致内存泄露；

### 如何防止发送队列积压
高并发场景下，由于服务端处理慢导致客户端消息挤压，服务端流控，客户端也需要做并发包含，防止自身消息积压；
Netty 提供的高水位机制，可以实现客户端更精准的流控，
- 当发送队列发送的字节数组达到高水位时，对应的channel变成不可写的状态，由于高水位并不影响业务线程调用write方法并把消息加入待发送队列，
因此，必须在消息发送时对channel状态进行判定，当到达高水位时，channel状态设置为不可写，通过channel的可写状态判定是否发送消息；

#### 其他可能导致发送队列积压
1.0 网络瓶颈
2.0 当对端读取速度小于己方发送速度时，导致自身的tcp发送缓存满，频繁发生write 0字节时，待发送的消息会在netty发送队列中积压

利用netstat -ano等命令可以监控某个端口下tcp 接收(recv -q) 和 发送队列(send -q) 的挤压情况；

## netty 消息发送机制
业务调用write方法后，经过ChannelPipeline职责链处理-->消息被投递到发送缓冲区待发送-->调动flush执行真正的发送操作；
-->底层调用Java NIO 的SocketChannel 进行非阻塞的Write处理，将消息发送到网络上；

Netty 的消息发送涉及到线程切换、消息队列、高低水位和写半包消息；

### WriteAndFlushTask 原理和源码解析
- Netty 采用了串行-无锁设计，在IO线程内部进行串行操作；通过调整NIO线程池的线程参数；同时启动多个串行化的线程并行运行；
- 当用户线程发起write操作，如果发现不是NioEventLoop(IO线程)，将发送消息封装成WriteTask；放入NioEventLoop的任务队列由NioEventLoop线程执行；
- Netty的NioEventLoop线程内部维护了一个Queue<Runnable> taskQueue 除了处理IO读写；同时还负责执行网络读写相关的Task;

### ChannelOutboundBuffer原理和源码分析
ChannelOutboundBuffer 是netty的发送缓冲队列，它基于链表管理待发送的消息；