package com.lgq.nio.buffere;

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
     * rewind??????????????????
     * ???????????? mark ????????????
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
        System.out.println("????????????");
        allocatTest();

        System.out.println("??????");
        putTest();

        System.out.println("??????");
        flipTest();

        System.out.println("??????");
        getTest();

        System.out.println("?????????");
        rewindTest();
        reRead();

        System.out.println("make&reset??????");
        afterReset();

        System.out.println("??????");
        clearDemo();
    }
}
