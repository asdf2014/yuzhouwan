package com.yuzhouwan.bigdata.redis.notification;

import com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import org.junit.Ignore;
import org.junit.Test;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
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
            assertEquals(null, pool.get("yuzhouwan02"));
            assertEquals("blog03", pool.get("yuzhouwan03"));

            Thread.sleep(1000);
            assertEquals(null, pool.get("yuzhouwan01"));
            assertEquals(null, pool.get("yuzhouwan02"));
            assertEquals("blog03", pool.get("yuzhouwan03"));

            assertEquals(true, 1 == pool.del("yuzhouwan03"));
            assertEquals(null, pool.get("yuzhouwan01"));
            assertEquals(null, pool.get("yuzhouwan02"));
            assertEquals(null, pool.get("yuzhouwan03"));
        }
    }
}