package com.lgq.jnetty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author lgq
 */
public class Integer2StringDecoderETest {
    /**
     * 整数to 字符串解码器的使用实例
     */
    @Test
    public void testIntegerToStringDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoderE());
                ch.pipeline().addLast(new Integer2StringDecoderE());
                ch.pipeline().addLast(new StringProcessHandlerE());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);

        for (int j = 0; j < 10; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
