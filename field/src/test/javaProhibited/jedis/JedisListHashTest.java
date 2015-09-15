package javaProhibited.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdf2014 on 2015/9/7.
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

    public void addList(String key, List<? super String> list) {

        ShardedJedis jedis = pool.getResource();

        jedis.lpush(key, (String[]) list.toArray());

        pool.returnResourceObject(jedis);
    }

    public static void main(String[] args) {

        JedisListHashTest jedisListHashTest = new JedisListHashTest();

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("s");
        list.add("d");
        list.add("f");

        jedisListHashTest.addList("asdf", list);

    }

}
