package com.yuzhouwan.bigdata.redis.notification;

import com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import org.junit.Ignore;
import org.junit.Test;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;
import static org.junit.Assert.*;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Redis Notification Test
 *
 * @author Benedict Jin
 * @since 2017/7/26
 */
public class RedisNotificationTest {

    @Ignore
    @Test
    public void testExpire() throws Exception {

        DynamicPropUtils dp = DynamicPropUtils.getInstance();
        dp.add(PROJECT_NAME, PropUtils.getInstance().getProperties());

        try (RedisClusterConnPool pool = new RedisClusterConnPool(dp)) {
            pool.del("yuzhouwan01");
            pool.del("yuzhouwan02");
            pool.del("yuzhouwan03");

            pool.put("yuzhouwan01", "blog01");
            pool.put("yuzhouwan02", "blog02", 1000);
            pool.put("yuzhouwan03", "blog03");
            pool.expire("yuzhouwan01", 2);

            Thread.sleep(1100);
            assertEquals("blog01", pool.get("yuzhouwan01"));
            assertNull(pool.get("yuzhouwan02"));
            assertEquals("blog03", pool.get("yuzhouwan03"));

            Thread.sleep(1000);
            assertNull(pool.get("yuzhouwan01"));
            assertNull(pool.get("yuzhouwan02"));
            assertEquals("blog03", pool.get("yuzhouwan03"));

            assertEquals(1, (long) pool.del("yuzhouwan03"));
            assertNull(pool.get("yuzhouwan01"));
            assertNull(pool.get("yuzhouwan02"));
            assertNull(pool.get("yuzhouwan03"));
        }
    }
}