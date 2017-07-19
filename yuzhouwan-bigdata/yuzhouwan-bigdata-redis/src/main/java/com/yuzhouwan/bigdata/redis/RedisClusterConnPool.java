package com.yuzhouwan.bigdata.redis;

import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Redis Cluster Conn Pool
 *
 * @author Benedict Jin
 * @since 2017/7/18
 */
public class RedisClusterConnPool {

    private static final Logger _log = LoggerFactory.getLogger(RedisClusterConnPool.class);

    private static JedisCluster pool;
    public static final String PROJECT_NAME = "REDIS_CLUSTER";

    public RedisClusterConnPool() {
        init(DynamicPropUtils.getInstance());
    }

    public RedisClusterConnPool(DynamicPropUtils DP) {
        init(DP);
    }

    private void init(DynamicPropUtils DP) {
        Object clusterListObj = DP.get(PROJECT_NAME, "redis.cluster.list");
        String clusterList;
        if (clusterListObj == null || StrUtils.isEmpty(clusterList = clusterListObj.toString())) {
            String error = String.format("Cannot get [%s-redis.cluster.list] from Dynamic PropUtils!", PROJECT_NAME);
            _log.error(error);
            throw new RuntimeException(error);
        }
        String[] hostAndPort;
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        for (String clusters : clusterList.split(",")) {
            hostAndPort = clusters.split(":");
            jedisClusterNodes.add(new HostAndPort(hostAndPort[0], Integer.valueOf(hostAndPort[1])));
        }
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(1000);
        conf.setMinIdle(50);
        conf.setMaxIdle(100);
        conf.setMaxWaitMillis(6 * 1000);
        conf.setTestOnBorrow(true);
        pool = new JedisCluster(jedisClusterNodes, conf);
    }

    public String put(String key, String value) {
        try {
            return pool.set(key, value);
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
    }

    public String get(String key) {
        try {
            return pool.get(key);
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
    }

    public Long putSet(String key, String... values) {
        try {
            return pool.sadd(key, values);
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
    }

    public Set<String> getSet(String key) {
        try {
            return pool.smembers(key);
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
    }

    public void close() {
        try {
            if (pool != null) pool.close();
        } catch (IOException e) {
            _log.error("", e);
        }
    }
}
