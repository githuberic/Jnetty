package com.lgq.netty.cases.chapter4;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author lgq
 */
public class HttpResponse {
    private HttpHeaders header;
    private FullHttpResponse response;
    private byte[] body;

    public HttpResponse(FullHttpResponse response) {
        this.header = response.headers();
        this.response = response;
        if (response.content() != null) {
            body = new byte[response.content().readableBytes()];
            response.content().getBytes(0, body);
        }
    }
    public byte[] body() {
        return body;
    }

    public HttpHeaders header() {
        return header;
    }

    /*
    public HttpResponse(FullHttpResponse response) {
        this.header = response.headers();
        this.response = response;
    }
    */

    /**
     * 1：Exception in thread "main" io.netty.util.IllegalReferenceCountException: refCnt: 0 操作了被释放的对象
     * 2：获取http response时非法引用异常，说明http body已被释放了，业务代码并没有主动释放bytebuf
     * @return
     */
    /*
    public byte[] body() {
        body = new byte[response.content().readableBytes()];
        response.content().getBytes(0, body);
        return body;
    }*/

    /**
     * Exception in thread "main" java.lang.UnsupportedOperationException: direct buffer
     * 1：response.content().array() 底层调用 pooledUnsafeDirectByteBuf 其并支持array
     * 2：netty 默认的 io buffer 使用直接内存directbytebuf 减少jvm从用户态到内核态socket的拷贝 "零copy"，直接内存无法转换成堆内存 因此不支持array()
     * @return
     */
    /*
    public byte[] body() {
        return body = response.content() != null ?
                response.content().array() : null;
    }*/
}
