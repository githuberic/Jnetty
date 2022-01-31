package com.lgq.jnetty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author lgq
 */
public class String2IntegerEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext c, String s, List<Object> list) throws Exception {
        char[] array = s.toCharArray();
        for (char a : array) {
            if (a >= 48 && a <= 57) {
                list.add(new Integer(a));
            }
        }
    }
}
