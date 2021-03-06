# MQTT 服务接入超时

### 现像
现象：生产环境的MQTT服务运行一段时间后，新的端设备无法接入，连接超时，分析MQTT服务端日志，没有明显异常，但内存占用高，查看连接数十万个TCP连接
处于ESTABLISHED, 实际的MQTT连接数应该在1万左右；

MQTT-服务端内存按照2万左右的连接数规模配置的，连接数达到10万+规模后，出现了服务端大量SocketChannel挤压、内存暴增、高频率GC和较长的STW时间，
对端侧设备的造成影响，部分设备握手超时，无法接入；

### 连接数膨胀原因分析
通过抓包发现，一些端设备并没有按照MQTT协议规范进行处理，包括
- 客户端发送CONNECT后连接，SSL握手成功之后没有按照协议规范继续处理，例如发送ping指令；
- 客户端发送TCP连接，不做SSL握手，也不做后续处理，导致TCP连接被挂起；

### 无效连接的关闭策略
创建链路空闲检测定时任务、将定时任务添加到channel绑定的NioEventloop中，到达超时时间，说明客户端MQTT连接没有创建成功，主动关闭连接

## 基于Netty可靠性设计
### 业务定制I/O异常
Netty的处理策略：发送IO时，底层的资源由Netty释放，同时将异常堆栈信息以事件的形式通知上层用户，由用户对异常进行定制；

### 链路有效性检测
要解决链路可靠性问题，必须周期性的读链路进行有效性检测，目前 最流行通用做法：心跳检测，

心跳检测机制分为三个层面
- TCP层面：TCP keep-alive机制，它的作用域：tcp协议栈
- 协议层：主要存在于长连接协议中，比如 MQTT
- 应用层：应用自行约定

netty提供了 idlestatehandler 业务层的心跳检测

### 内存保护
- 链路总数的控制：每条链路都包含接收和发送缓冲区，链路个数太多容易导致内存溢出
- 单个缓冲区的上限限制：防止非法长度或消息过大导致内存溢出
- 缓冲区内存释放 防止因为缓冲区使用不当导致内存泄露
- NIO消息发送队列的长度上限控制
1. 防止内存池泄露
2. 缓冲区溢出保护
    - 对消息进行解码时，需要创建缓冲区(netty/bytebuf),缓冲区的创建方式通常有2种
        1. 容量预分配，在实际读写过程中如果不够再扩展
        2. 根据协议消息长度创建缓冲区
    - netty提供编解码框架，在实际工作中，有两种方式对缓冲区进行保护
        1. 创建bytebuf时，对它的容量上限进行限制
        2. 在消息解码时，对消息长度进行判断，如果超过最大容量，则thorw解码异常，拒绝分配内存
3. 消息发送队列积压保护
netty 的nio消息发送队列channneloutboundbuffer并没有容量上限，它会随着消息的积压自动扩展，直到达到0x7fffffff,
如果对端消息处理慢，会导致tcp滑窗长时间为0，如果消息发送方发送速度过快或一次批量发送消息过大，会导致channneloutboundbuffer内存膨胀，会使系统内存溢出
   



