package com.yuzhouwan.bigdata.redis.conn;

import com.alibaba.fastjson.JSON;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import org.junit.*;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Copyright @ 2019 yuzhouwan.com
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
    public void init() {
        DynamicPropUtils DP = DynamicPropUtils.getInstance();
        DP.add(PROJECT_NAME, PropUtils.getInstance().getProperties());
        store = new RedisClusterConnPool(DP);
    }

    @Ignore
    @Test
    public void testRedisCluster() {
        assertEquals("OK", store.put("yuzhouwan", "com"));
        assertEquals("com", store.get("yuzhouwan"));
        assertTrue(0 == store.putSet("topics", "bigdata", "ai"));
        assertEquals("[\"bigdata\",\"ai\"]", JSON.toJSONString(store.getSet("topics")));
    }

    @Ignore
    @After
    public void close() {
        store.close();
    }
}
