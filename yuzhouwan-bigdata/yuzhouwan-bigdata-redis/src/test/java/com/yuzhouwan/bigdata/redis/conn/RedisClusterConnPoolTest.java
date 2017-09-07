package com.yuzhouwan.bigdata.redis.conn;

import com.alibaba.fastjson.JSON;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;
import static org.junit.Assert.assertEquals;


/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Redis Cluster Conn Pool Test
 *
 * @author Benedict Jin
 * @since 2017/7/18
 */
public class RedisClusterConnPoolTest {

    private static RedisClusterConnPool store;

    @Ignore
    @Before
    public void init() throws Exception {
        DynamicPropUtils DP = DynamicPropUtils.getInstance();
        DP.add(PROJECT_NAME, PropUtils.getInstance().getProperties());
        store = new RedisClusterConnPool(DP);
    }

    @Ignore
    @Test
    public void testRedisCluster() throws Exception {
        assertEquals("OK", store.put("yuzhouwan", "com"));
        assertEquals("com", store.get("yuzhouwan"));
        assertEquals(true, 0 == store.putSet("topics", "bigdata", "ai"));
        assertEquals("[\"bigdata\",\"ai\"]", JSON.toJSONString(store.getSet("topics")));
    }

    @Ignore
    @After
    public void close() throws Exception {
        store.close();
    }
}
