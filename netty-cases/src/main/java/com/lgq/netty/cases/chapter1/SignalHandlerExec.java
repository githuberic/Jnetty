package com.lgq.netty.cases.chapter1;

import sun.misc.Signal;

import java.util.concurrent.TimeUnit;

/**
 * @author lgq
 * Java 优雅退出机制 jdk shutdownhook，接收系统退出时接收到的指令
 */
public class SignalHandlerExec {
    public static void main(String[] args) {
        //Windows 退出信号
        Signal sig = new Signal("INT");

        Signal.handle(sig, signal -> {
            System.out.println("Signal handle start...");
            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            System.out.println("ShutdownHook execute start...");
            System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ShutdownHook execute end...");
        }, ""));

        new Thread(() -> {
            try {
                TimeUnit.DAYS.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Daemon-T").start();
    }
}
