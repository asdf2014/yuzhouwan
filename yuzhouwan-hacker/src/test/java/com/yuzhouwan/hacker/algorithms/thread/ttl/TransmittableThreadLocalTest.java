package com.yuzhouwan.hacker.algorithms.thread.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Transmittable ThreadLocal Test
 *
 * @author Benedict Jin
 * @since 2018/6/7
 */
public class TransmittableThreadLocalTest {

    @Test
    public void parentChildThread() {
        ThreadLocal<String> tl = new ThreadLocal<>();
        tl.set("tl");
        TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<>();
        ttl.set("ttl");

        new Thread(() -> {
            assertEquals(tl.get(), null);
            assertEquals(ttl.get(), "ttl");
        }).start();
    }
}
