package com.yuzhouwan.site.hacker.javaProhibited.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benedict Jin on 2015/9/7.
 */
public class JedisListHashTest {

    private ShardedJedisPool pool;

    public JedisListHashTest() {
        init();
    }

    private void init() {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();

        JedisShardInfo si = new JedisShardInfo("localhost", 6379);
        si.setPassword("asdf");
        shards.add(si);

        si = new JedisShardInfo("localhost", 6380);
        si.setPassword("asdf");
        shards.add(si);

        pool = new ShardedJedisPool(new GenericObjectPoolConfig(), shards);
    }

    public void addList(String key, String[] list) {

        ShardedJedis jedis = pool.getResource();

        jedis.lpush(key, list);

        pool.returnResourceObject(jedis);
    }

    public static void main(String[] args) {

        JedisListHashTest jedisListHashTest = new JedisListHashTest();

        String[] list = {"a", "s", "d", "f"};

        jedisListHashTest.addList("asdf", list);

    }

}
