package com.yuzhouwan.hacker.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Netty Test
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/200316/">Netty：从入门到实践</a>
 * @since 2020/5/7
 */
public class NettyTest {

    @Test
    public void string2ByteBuf() {
        String s = "yuzhouwan.com";
        final ByteBuf buf = Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        Assert.assertEquals(s, new String(bytes, StandardCharsets.UTF_8));
    }
}
