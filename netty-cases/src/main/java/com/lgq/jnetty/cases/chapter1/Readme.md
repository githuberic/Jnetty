## Chapter1

### netty 优雅退出
- 预处理的操作
    - 通信队列中尚未发送的消息
    - NIO线程中待处理的定时任务
    - 注册到NIO线程中shutdownhook任务
- 资源释放
    - 关闭所有的channel
    - 从selector上注册
    - 清空所有的队列
    - NIO线程退出

## Java优雅退出机制
- 注册shutdownhook
通过JDK shutdownhook来实现，当系统接收到退出指令时，
首先标记 系统处于退出状态，不再接收新的消息，然后将挤压的消息处理完毕，最后调用资源回收接口将资源回收，各线程退出执行
- 监听信号量并注册SignalHandler的方式实现优雅退出
### SignalHandler 处理流程
- 初始化Signal
- 根据操作系统选择信号量
- 获取并注册SignalHandler
- 在handler(Signal sgin)根据信号量类型经相应操作

