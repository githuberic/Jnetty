package com.lgq.nio.bufferE;

import java.nio.IntBuffer;

/**
 * @author lgq
 */
public class UseBuffer {
    static IntBuffer intBuffer = null;

    public static void allocatTest() {
        intBuffer = IntBuffer.allocate(20);
        System.out.println("------------after allocate------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    public static void putTest() {
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }

        System.out.println("------------after putTest------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    public static void flipTest() {
        intBuffer.flip();
        System.out.println("------------after flipTest ------------------");

        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    public static void getTest() {
        for (int i = 0; i < 2; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
        System.out.println("------------after get 2 int ------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
        
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
        System.out.println("------------after get 3 int ------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }


    public static void rewindTest() {
        intBuffer.rewind();
        System.out.println("------------after rewind ------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    /**
     * rewind之后，重复读
     * 并且演示 mark 标记方法
     */
    public static void reRead() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                intBuffer.mark();
            }
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
       
        System.out.println("------------after reRead------------------");
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    public static void afterReset() {
        System.out.println("------------after reset------------------");
        intBuffer.reset();
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
        for (int i =2; i < 5; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
    }

    public static void clearDemo() {
        System.out.println("------------after clear------------------");
        intBuffer.clear();
        System.out.println("position=" + intBuffer.position());
        System.out.println("limit=" + intBuffer.limit());
        System.out.println("capacity=" + intBuffer.capacity());
    }

    public static void main(String[] args) {
        System.out.println("分配内存");
        allocatTest();

        System.out.println("写入");
        putTest();

        System.out.println("翻转");
        flipTest();

        System.out.println("读取");
        getTest();

        System.out.println("重复读");
        rewindTest();
        reRead();

        System.out.println("make&reset写读");
        afterReset();

        System.out.println("清空");
        clearDemo();
    }
}
