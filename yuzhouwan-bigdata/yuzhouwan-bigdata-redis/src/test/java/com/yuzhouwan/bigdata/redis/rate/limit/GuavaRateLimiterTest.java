package com.yuzhouwan.bigdata.redis.rate.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Guava Rate Limiter Test
 *
 * @author Benedict Jin
 * @since 2018/1/9
 */
public class GuavaRateLimiterTest {

    private static final int PERMITS_PER_SECOND = 2;
    private static final int PERMITS_NEEDS_PER_OPERATION = 1;
    private static final int SHOULD_WAIT_MILLISECONDS = 1000;
    private static final int FOR_FAULT_TOLERANT_MILLISECONDS = 200;

    @Test
    public void normal() {
        RateLimiter rateLimiter = RateLimiter.create(PERMITS_PER_SECOND);
        long startTime = System.currentTimeMillis();
        rateLimiter.acquire(PERMITS_NEEDS_PER_OPERATION);
        System.out.println("Operation 1.");
        rateLimiter.acquire(PERMITS_NEEDS_PER_OPERATION);
        System.out.println("Operation 2.");
        rateLimiter.acquire(PERMITS_NEEDS_PER_OPERATION);
        System.out.println("Operation 3.");
        long usedTime = System.currentTimeMillis() - startTime;
        System.out.println(usedTime + " ms");
        assertTrue(usedTime + FOR_FAULT_TOLERANT_MILLISECONDS >= SHOULD_WAIT_MILLISECONDS);
        System.out.println(String.format("%s >= %s" + " ms", usedTime, SHOULD_WAIT_MILLISECONDS));
    }
}
