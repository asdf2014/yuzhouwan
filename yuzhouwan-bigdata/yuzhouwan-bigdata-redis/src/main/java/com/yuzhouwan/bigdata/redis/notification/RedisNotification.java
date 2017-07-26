package com.yuzhouwan.bigdata.redis.notification;

import com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Redis Notification
 *
 * @author Benedict Jin
 * @since 2017/7/26
 */
public class RedisNotification {

    private static final Logger _log = LoggerFactory.getLogger(RedisNotification.class);

    public static void main(String[] args) throws Exception {

        DynamicPropUtils dp = DynamicPropUtils.getInstance();
        dp.add(PROJECT_NAME, PropUtils.getInstance().getProperties());

        /*
        https://redis.io/topics/notifications
        https://raw.githubusercontent.com/antirez/redis/2.8/redis.conf

        $ redis-cli -h localhost -p 6380
            CONFIG SET notify-keyspace-events AKE
            PSUBSCRIBE '__key*__:*'
            PUBLISH __keyevent@0__:expired yuzhouwan01

            SET yuzhouwan01 blog PX 3000 NX
            SET yuzhouwan02 blog
            EXPIRE yuzhouwan01 3

        $ redis-cli -h localhost -p 6380 --csv psubscribe '__keyevent@0__:expired'
        */
        try (RedisClusterConnPool pool = new RedisClusterConnPool(dp)) {
            JedisCluster jedis = pool.getPool();
            JedisPubSub jedisPubSub = new JedisPubSub() {

                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    _log.info("onPSubscribe {} {}", pattern, subscribedChannels);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    _log.info("onPMessage: {}, Channel: {}, Message: {}", pattern, channel, message);
                    if ("__keyevent@0__:expired".equals(channel)) {
                        _log.info("onPMessage: {}, Channel: {}, Message: {}", pattern, channel, message);
                    }
                }
            };
            jedis.psubscribe(jedisPubSub, /*"__keyevent@*__:expired"*/ /*"__key*__:*"*/ "*");
        }
    }
}
