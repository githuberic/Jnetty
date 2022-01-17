package com.lgq.jnetty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * @author lgq
 */
public class ReferenceE {
    @Test
    public void testRef() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println("after create:" + buffer.refCnt());
        buffer.retain();
        System.out.println("after retain:" + buffer.refCnt());
        buffer.release();
        System.out.println("after release:" + buffer.refCnt());
        buffer.release();
        System.out.println("after release:" + buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        System.out.println("after retain:" + buffer.refCnt());
    }
}
