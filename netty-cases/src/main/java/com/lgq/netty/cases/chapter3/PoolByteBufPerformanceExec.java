package com.lgq.netty.cases.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author lgq
 */
public class PoolByteBufPerformanceExec {
    public static void main(String[] args) {
        unPoolTest();
    }

    static void unPoolTest()
    {
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 100000000;
        for(int i = 0; i < maxTimes; i++)
        {
            buf = Unpooled.buffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : " + (System.currentTimeMillis() - beginTime));
    }

    static void poolTest()
    {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 100000000;
        for(int i = 0; i < maxTimes; i++)
        {
            buf = allocator.heapBuffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : "
                + (System.currentTimeMillis() - beginTime));
    }
}
