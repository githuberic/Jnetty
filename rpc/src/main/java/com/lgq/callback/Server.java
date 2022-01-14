package com.lgq.callback;

public abstract class Server {
    // 服务启动后回调
    private BaseCallBack startedCallBack;

    // 服务停止后回调
    private BaseCallBack stopedCallBack;

    /**
     * 设置服务启动后的回调逻辑
     *
     * @param startedCallBack
     */
    public void setStartedCallBack(BaseCallBack startedCallBack) {
        this.startedCallBack = startedCallBack;
    }

    /**
     * 设置服务停止后的回调逻辑
     *
     * @param stopedCallBack
     */
    public void setStopedCallBack(BaseCallBack stopedCallBack) {
        this.stopedCallBack = stopedCallBack;
    }

    /**
     * 服务启动后回调
     */
    public void onStarted() {
        if (startedCallBack != null) {
            try {
                startedCallBack.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务停止后回调
     */
    public void onStoped() {
        if (stopedCallBack != null) {
            try {
                stopedCallBack.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动服务
     *
     * @param provider
     * @throws Exception
     */
    public abstract void start(Provider provider) throws Exception;

    /**
     * 停止服务
     *
     * @throws Exception
     */
    public abstract void stop() throws Exception;
}
