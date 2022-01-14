package com.lgq.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lgq
 */
public class CodecApp {
    public static void main(String[] args) {
        String expect = "hello, world.";

        ByteBuf byteBuf = Unpooled.buffer(1024);

        RpcEncoder encoder = new RpcEncoder(String.class);
        encoder.encode(null, expect, byteBuf);

        List<Object> decoded = new ArrayList<>();
        RpcDecoder decoder = new RpcDecoder(String.class);
        decoder.decode(null, byteBuf, decoded);

        if (expect.equals(decoded.get(0))) {
            System.out.println("equal");
        } else {
            System.out.println("not equal");
        }
    }
}
