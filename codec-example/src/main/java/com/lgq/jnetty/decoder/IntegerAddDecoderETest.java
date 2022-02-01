package com.lgq.jnetty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author lgq
 */
public class IntegerAddDecoderETest {
    @Test
    public void testByteToIntegerDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new IntegerAddDecoderE());
                ch.pipeline().addLast(new IntegerProcessHandlerE());
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
