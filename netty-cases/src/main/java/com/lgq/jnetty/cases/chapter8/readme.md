# 车联网服务端接收不到车载终端消息案例
服务端运行一段时间，发现无法接收到车载终端的消息(间隔一段又恢复了)；经排查CPU、内存、网络都不是瓶颈
- 从线程堆栈信息来看，netty/nioeventloop收到消息后，调用业务线程池执行业务逻辑时，rejectedexecutionexception,
后续业务由nioeventloop执行，业务使用了callerrunspolicy策略，即在业务线程池满了之后，由调用方线程来执行当前的runnable，
- NioEventLoop在执行业务任务时发生了阻塞，导致nioeventloop线程无法处理网络消息，因此服务端没有消息接入，从阻塞状态恢复后，就可以继续接收消息；

## nioeventloop线程防挂死策略
channelhandler是业务代码和netty框架交汇的地方，channelhandler里面的代码通常由nioeventloop执行，因此防止业务代码阻塞非常重要；常见的阻塞情况
1. 直接在channelhandler里面写可能导致程序阻塞的代码，包含但不限于 数据库操作、第三方服务调用、中间件服务调用、同步获取锁、sleep等
2. 切换到业务线程池时或者业务消息队列做异步处理时发生了阻塞，比如 阻塞队列，同步获取锁等；
