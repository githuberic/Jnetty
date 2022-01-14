package com.lgq.callback;

/**
 * @author lgq
 */
public class AppMain {
    // TCP 服务器IP
    public static String tcpSrvAddr = "192.168.11.23";
    // TCP 服务端口
    public static int tcpSrvPort = 9090;

    public static void main(String[] args) {
        Provider provider = new Provider();
        provider.initConfig(tcpSrvAddr, tcpSrvPort);
        provider.start();
        provider.stop();
    }
}
