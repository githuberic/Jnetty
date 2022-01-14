package com.lgq.callback;

import java.util.TreeSet;

/**
 * @author lgq
 */
public class Provider {
    // 模拟要注册的服务列表
    public static TreeSet<String> serviceKeys = new TreeSet<String>() {{
        add("userService");
        add("productService");
        add("orderService");
    }};

    // 模拟本机http服务使用的ip和端口
    public static String localAddress = "127.0.0.1:8081";

    // TCP服务器地址
    private String tcpSrvAddr;

    // TCP服务器端口
    private int tcpSrvPort;

    public String getTcpSrvAddr() {
        return tcpSrvAddr;
    }

    public int getTcpSrvPort() {
        return tcpSrvPort;
    }

    private Server server;
    private Registry registry;

    public Provider() {
    }

    /**
     * 初始化配置
     *
     * @param tcpSrvAddr
     * @param tcpSrvPort
     */
    public void initConfig(String tcpSrvAddr, int tcpSrvPort) {
        this.tcpSrvAddr = tcpSrvAddr;
        this.tcpSrvPort = tcpSrvPort;
    }

    /**
     * 启动服务
     */
    public void start() {
        try {
            registry = Registry.class.newInstance();
            server = TcpServerImpl.class.newInstance();
            // 设置服务启动后回调逻辑
            server.setStartedCallBack(new BaseCallBack() {
                @Override
                public void run() {
                    System.out.println(">>>> setStartedCallBack:" + serviceKeys + ":" + localAddress);
                    // 注册服务
                    registry.start();
                    registry.registry(serviceKeys, localAddress);
                }
            });

            // 设置服务停止后回调逻辑
            server.setStopedCallBack(new BaseCallBack() {
                @Override
                public void run() {
                    System.out.println(">>>> setStopedCallBack:" + tcpSrvAddr + ":" + tcpSrvPort);
                    registry.remove(serviceKeys, localAddress);
                }
            });

            // 启动服务
            server.start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
