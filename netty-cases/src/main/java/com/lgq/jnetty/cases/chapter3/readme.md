# netty 内存池
netty针对bytebuf的申请和释放采用了池化技术，通过pooledByteBufAllocator 可以基于内存池分配bytebuf对象

## bytebuf
netty默认采用内存池模式创建bytebuf,其性能比传统非池化方式高8倍多；

### netty内存池结构
整体上参考jemolloc 实现，主要的数据结构
- PooledArea
- PoolChunk
- Poolsubpage
- page