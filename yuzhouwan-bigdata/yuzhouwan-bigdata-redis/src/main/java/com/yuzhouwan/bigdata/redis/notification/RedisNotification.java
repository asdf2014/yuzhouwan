package com.yuzhouwan.bigdata.redis.notification;

import com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool;
import com.yuzhouwan.common.util.DynamicPropUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.yuzhouwan.bigdata.redis.conn.RedisClusterConnPool.PROJECT_NAME;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Redis Notification
 *
 * @author Benedict Jin
 * @since 2017/7/26
 */
public class RedisNotification {

    private static final Logger _log = LoggerFactory.getLogger(RedisNotification.class);
    private static final long CHECK_EXECUTOR_SERVICE_INTERVAL_MILLISECONDS = 1000 * 60 * 60 * 24;

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
            EXPIRE yuzhouwan02 3

        $ redis-cli -h localhost -p 6380 --csv psubscribe '__keyevent@0__:expired'
        */
        ExecutorService es = ThreadUtils.buildExecutorService("redis-subscribe");
        try (RedisClusterConnPool pool = new RedisClusterConnPool(dp, true)) {
            List<JedisPool> jedis = pool.getPools();
            JedisPubSub jedisPubSub = new JedisPubSub() {

                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    _log.info("onPSubscribe Pattern: {}, SubscribedChannels: {}", pattern, subscribedChannels);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    _log.info("onPMessage Pattern: {}, Channel: {}, Message: {}", pattern, channel, message);
                }
            };
            for (JedisPool j : jedis)
                es.submit(() ->
                        j.getResource().psubscribe(jedisPubSub, "__keyevent@*__:expired" /*"__key*__:*"*/ /*"*"*/)
                );
            while (!es.isTerminated()) Thread.sleep(CHECK_EXECUTOR_SERVICE_INTERVAL_MILLISECONDS);
        }
    }
}
