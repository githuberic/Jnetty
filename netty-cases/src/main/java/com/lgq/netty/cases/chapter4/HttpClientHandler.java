package com.lgq.netty.cases.chapter4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.DefaultPromise;

/**
 * @author lgq
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if (msg.decoderResult().isFailure()) {
            throw new Exception("Decode HttpResponse error : " + msg.decoderResult().cause());
        }

        // 执行完毕channelRead0后，netty/nioeventloop线程会调用referenceCountUtil.release(msg)释放
        // 后续业务方的线程再访问fullhttpresponse会出现非法引用问题
        HttpResponse response = new HttpResponse(msg);

        // 调用业务thread继续执行
        respPromise.setSuccess(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    DefaultPromise<HttpResponse> respPromise;
    public DefaultPromise<HttpResponse> getRespPromise() {
        return respPromise;
    }
    public void setRespPromise(DefaultPromise<HttpResponse> respPromise) {
        this.respPromise = respPromise;
    }
}
