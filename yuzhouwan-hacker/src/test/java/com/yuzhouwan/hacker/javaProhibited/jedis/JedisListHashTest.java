package com.yuzhouwan.hacker.javaProhibited.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Jedis List Hash Test
 *
 * @author Benedict Jin
 * @since 2015/9/7
 */
public class JedisListHashTest {

    private JedisPool pool;

    public JedisListHashTest() {
        init();
    }

    public static void main(String[] args) {
        JedisListHashTest jedisListHashTest = new JedisListHashTest();
        String[] list = {"a", "s", "d", "f"};
        jedisListHashTest.addList("asdf", list);
    }

    private void init() {
        pool = new JedisPool("localhost", 6379);
    }

    public void addList(String key, String[] list) {
        try (Jedis jedis = pool.getResource()) {
            jedis.lpush(key, list);
        }
    }
}
