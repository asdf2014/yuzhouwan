package com.yuzhouwan.hacker.algorithms.thread.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        String tlMsg = "tl";
        String ttlMsg = "ttl";

        final ThreadLocal<String> tl = new ThreadLocal<>();
        tl.set(tlMsg);
        final TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<>();
        ttl.set(ttlMsg);

        assertEquals(tl.get(), tlMsg);
        assertEquals(ttl.get(), ttlMsg);

        new Thread(() -> {
            assertNull(tl.get());
            assertEquals(ttl.get(), ttlMsg);
        }).start();
    }
}
