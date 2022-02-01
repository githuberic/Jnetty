package com.lgq.jnetty.decoder;

import com.lgq.jnetty.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @author lgq
 */
public class StringHeaderDecoderETest {
    static String content = "老刘-lgq-netty";

    /**
     * 字符串解码器的使用实例
     */
    @Test
    public void testStringReplayDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new StringHeaderDecoderE());
                ch.pipeline().addLast(new StringProcessHandlerE());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for (int j = 0; j < 100; j++) {
            //1-3之间的随机数
            int random = RandomUtil.randInMod(3);
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(bytes.length * random);
            for (int k = 0; k < random; k++) {
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
