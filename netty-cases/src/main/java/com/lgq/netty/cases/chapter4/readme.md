# ByteBuf
Byte数组缓冲区，和java/bytebuffer一致，提供以下基本功能
- 7种Java基础类型 byte数组 bytebuffer 等读写
- 缓冲区自身的copy、slice等
- 操作位置指针等方法
- 容量自动扩展
- 从bytebuf到其他数据类型转换灵活
- 2个位置指针协助缓存区读写操作(readerindex),writerindex