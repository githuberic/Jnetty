package com.lgq.jnetty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author lgq
 */
public class Byte2IntegerReplayDecoderTest {
    /**
     * 整数解码器的使用实例
     */
    @Test
    public void testByte2IntegerReplayDecoder() {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerReplayDecoderE());
                ch.pipeline().addLast(new IntegerProcessHandlerE());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);

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
