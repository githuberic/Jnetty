package com.lgq.callback;

/**
 * @author lgq
 */
public class TcpServerImpl extends Server {

    /**
     * 启动服务
     *
     * @param provider
     */
    @Override
    public void start(Provider provider) {
        System.out.println(">>>> start! " + provider.getTcpSrvAddr() + ":" + provider.getTcpSrvPort());
        // 启动后回调
        onStarted();

    }

    /**
     * 停止服务
     */
    @Override
    public void stop() {
        System.out.println(">>>> stop!");
        // 停止后回调
        onStoped();
    }
}
