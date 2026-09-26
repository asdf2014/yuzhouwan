package com.yuzhouwan.bigdata.redis.conn;

import com.yuzhouwan.common.util.DynamicPropUtils;
import org.junit.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;


/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Redis Cluster Conn Pool Test
 *
 * @author Benedict Jin
 * @since 2017/7/18
 */
public class RedisClusterConnPoolTest {

    private RedisClusterConnPool store;
    private DynamicPropUtils properties;
    private String prefix;

    @Before
    public void init() {
        String nodes = System.getProperty("redis.cluster.nodes");
        assumeNotNull(nodes);
        Properties config = new Properties();
        config.setProperty("redis.cluster.list", nodes);
        properties = DynamicPropUtils.getInstance();
        properties.add(PROJECT_NAME, config);
        store = new RedisClusterConnPool(properties);
        prefix = "jedis-integration-" + UUID.randomUUID();
    }

    @Test
    public void testRedisCluster() {
        String key = prefix + ":value";
        assertEquals("OK", store.put(key, "com"));
        assertEquals("com", store.get(key));
        assertEquals(Long.valueOf(1), store.del(key));
        assertNull(store.get(key));

        assertEquals("OK", store.put(key, "first", 30000));
        assertNull(store.put(key, "second", 30000));
        assertEquals("first", store.get(key));
        assertTrue(store.getCluster().pttl(key) > 0);
        assertEquals(Long.valueOf(1), store.expire(key, 60));
        assertTrue(store.getCluster().ttl(key) <= 60);

        String topics = prefix + ":topics";
        assertEquals(Long.valueOf(2), store.putSet(topics, "bigdata", "ai"));
        assertEquals(Set.of("bigdata", "ai"), store.getSet(topics));

        String list = prefix + ":list";
        assertEquals(Long.valueOf(2), store.pushList(list, "first", "second"));
        assertEquals(List.of("second", "first"), store.getListAll(list));

        String hash = prefix + ":hash";
        assertEquals(Long.valueOf(1), store.putHash(hash, "site", "yuzhouwan.com"));
        assertEquals("yuzhouwan.com", store.getHash(hash, "site"));
        assertEquals(Map.of("site", "yuzhouwan.com"), store.getHashs(hash));
        assertEquals(Long.valueOf(1), store.delHash(hash, "site"));
    }

    @After
    public void close() {
        if (store != null) {
            try {
                for (String suffix : List.of(":value", ":topics", ":list", ":hash")) {
                    store.del(prefix + suffix);
                }
            } finally {
                store.close();
            }
        }
        if (properties != null) properties.close();
    }
}
