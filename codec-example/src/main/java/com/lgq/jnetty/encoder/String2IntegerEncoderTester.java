package com.lgq.jnetty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author lgq
 */
public class String2IntegerEncoderTester {
    /**
     * 测试字符串到整数编码器
     */
    @Test
    public void testStringToIntergerDecoder() {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Integer2ByteEncoder());
                ch.pipeline().addLast(new String2IntegerEncoder());
            }
        };

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInitializer);

        for (int j = 0; j < 10; j++) {
            String s = "i am " + j;
            embeddedChannel.write(s);
        }
        embeddedChannel.flush();

        ByteBuf buf = (ByteBuf) embeddedChannel.readOutbound();
        while (null != buf) {
            System.out.println("o = " + buf.readInt());
            buf = (ByteBuf) embeddedChannel.readOutbound();
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
