package com.yuzhouwan.bigdata.redis.conn;

import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.RandomUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Redis Cluster Conn Pool
 *
 * @author Benedict Jin
 * @since 2017/7/18
 */
public class RedisClusterConnPool implements AutoCloseable, Serializable {

    public static final String PROJECT_NAME = "REDIS_CLUSTER";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConnPool.class);
    private JedisCluster cluster;
    private List<JedisPool> pools;

    public RedisClusterConnPool() {
        init(DynamicPropUtils.getInstance());
    }

    public RedisClusterConnPool(DynamicPropUtils dp) {
        init(dp);
    }

    public RedisClusterConnPool(boolean pools) {
        DynamicPropUtils dp = DynamicPropUtils.getInstance();
        if (pools) initPools(dp);
        else init(dp);
    }

    public RedisClusterConnPool(DynamicPropUtils dp, boolean pools) {
        if (pools) initPools(dp);
        else init(dp);
    }

    private void init(DynamicPropUtils dp) {
        String clusterList = getClusterList(dp);
        String[] hostAndPort;
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        for (String clusters : clusterList.split(",")) {
            hostAndPort = clusters.split(":");
            jedisClusterNodes.add(new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        cluster = new JedisCluster(jedisClusterNodes, buildConf());
    }

    private void initPools(DynamicPropUtils dp) {
        String clusterList = getClusterList(dp);
        pools = new LinkedList<>();
        String[] hostAndPort;
        for (String clusters : clusterList.split(",")) {
            hostAndPort = clusters.split(":");
            pools.add(new JedisPool(buildConf(), hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
    }

    private String getClusterList(DynamicPropUtils dp) {
        Object clusterListObj = dp.get(PROJECT_NAME, "redis.cluster.list");
        String clusterList;
        if (clusterListObj == null || StrUtils.isEmpty(clusterList = clusterListObj.toString())) {
            String error = String.format("Cannot get [%s-redis.cluster.list] from Dynamic PropUtils!", PROJECT_NAME);
            LOGGER.error(error);
            throw new RuntimeException(error);
        }
        return clusterList;
    }

    private JedisPoolConfig buildConf() {
        // org.apache.commons.pool2.impl.BaseObjectPoolConfig
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(1000);
        conf.setMinIdle(50);
        conf.setMaxIdle(100);
        // conf.setMaxWaitMillis(6 * 1000);
        conf.setTestOnCreate(true);
        conf.setTestOnBorrow(true);
        conf.setTestOnReturn(true);
        conf.setTestWhileIdle(true);
        // conf.setTimeBetweenEvictionRunsMillis(1);
        conf.setNumTestsPerEvictionRun(30);
        return conf;
    }

    // k-v
    public String put(String key, String value) {
        try {
            return cluster.set(key, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public String put(String key, String value, long millisecond) {
        try {
            // NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
            // EX|PX, expire time units: EX = seconds; PX = milliseconds
            return cluster.set(key, value, "NX", "PX", millisecond);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long expire(String key, int second) {
        try {
            // NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
            // EX|PX, expire time units: EX = seconds; PX = milliseconds
            return cluster.expire(key, second);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public String get(String key) {
        try {
            return cluster.get(key);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long del(String key) {
        try {
            return cluster.del(key);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    // list
    public String setList(String key, String value) {
        return setList(key, RandomUtils.uuid(), value);
    }

    public String setList(String key, long index, String value) {
        try {
            return cluster.lset(key, index, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long pushList(String key, String... value) {
        try {
            return cluster.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long pushList(String key, Collection<String> values) {
        try {
            for (String value : values) cluster.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        return 0L;
    }

    public Long delElementFromList(String key, String e) {
        List<String> listAll = getListAll(key);
        if (listAll.contains(e)) listAll.remove(e);
        else return 0L;
        return pushList(key, listAll);
    }

    public List<String> getListAll(String key) {
        try {
            return cluster.lrange(key, 0, -1);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    // set
    public Long putSet(String key, String... values) {
        try {
            return cluster.sadd(key, values);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long putSet(String key, Collection<String> values) {
        try {
            for (String value : values) cluster.sadd(key, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        return 0L;
    }

    public Set<String> getSet(String key) {
        try {
            return cluster.smembers(key);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long delElementFromSet(String key, String value) {
        try {
            /*
            No way to dispatch this command to Redis Cluster because keys have different slots.
             */
            // return cluster.smove(key, NULL_KEY, value);
            Set<String> members = getSet(key);
            if (members.contains(value)) {
                members.remove(value);
                cluster.del(key);
                putSet(key, members);
            } else return 0L;
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        return 0L;
    }

    // hash
    public Long putHash(String key, String field, String value) {
        try {
            return cluster.hset(key, field, value);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public String getHash(String key, String field) {
        try {
            return cluster.hget(key, field);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Map<String, String> getHashs(String key) {
        try {
            return cluster.hgetAll(key);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public List<String> getHashValues(String key) {
        try {
            return cluster.hvals(key);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long delHash(String key, String... fields) {
        try {
            return cluster.hdel(key, fields);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public Long delHashs(String key) {
        try {
            for (String k : cluster.hkeys(key)) cluster.hdel(key, k);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        return 0L;
    }

    public JedisCluster getCluster() {
        return cluster;
    }

    public List<JedisPool> getPools() {
        return pools;
    }

    @Override
    public void close() {
        try {
            if (cluster != null) cluster.close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }
}
