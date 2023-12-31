package com.yuzhouwan.hacker.javaProhibited.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Jedis List Hash Test
 *
 * @author Benedict Jin
 * @since 2015/9/7
 */
public class JedisListHashTest {

    private ShardedJedisPool pool;

    public JedisListHashTest() {
        init();
    }

    public static void main(String[] args) {
        JedisListHashTest jedisListHashTest = new JedisListHashTest();
        String[] list = {"a", "s", "d", "f"};
        jedisListHashTest.addList("asdf", list);
    }

    private void init() {
        List<JedisShardInfo> shards = new ArrayList<>();

        JedisShardInfo si = new JedisShardInfo("localhost", 6379);
        si.setPassword("asdf");
        shards.add(si);

        si = new JedisShardInfo("localhost", 6380);
        si.setPassword("asdf");
        shards.add(si);

        pool = new ShardedJedisPool(new GenericObjectPoolConfig<>(), shards);
    }

    public void addList(String key, String[] list) {
        ShardedJedis jedis = pool.getResource();
        jedis.lpush(key, list);
        jedis.close();
    }
}
