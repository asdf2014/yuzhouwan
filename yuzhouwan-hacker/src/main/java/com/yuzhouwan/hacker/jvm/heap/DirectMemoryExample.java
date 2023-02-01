package com.yuzhouwan.hacker.jvm.heap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：DirectMemory Example
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/200316/">Netty：从入门到实践</a>
 * @since 2020/5/20
 */
public class DirectMemoryExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        final Field f = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
        f.setAccessible(true);
        AtomicLong directMemory = (AtomicLong) f.get(PlatformDependent.class);
        System.out.println("1) DIRECT_MEMORY_COUNTER: " + directMemory);
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.directBuffer();
            buffer.writeBytes("yuzhouwan.com".getBytes());
            directMemory = (AtomicLong) f.get(PlatformDependent.class);
            System.out.println("2) DIRECT_MEMORY_COUNTER: " + directMemory);
        } finally {
            if (buffer != null) {
                buffer.clear();
            }
        }
    }
}
