package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Alive Connection Pool
 *
 * @author Benedict Jin
 * @since 2015/12/2
 */
public class KafkaConnPoolUtils {

    private static final Logger _log = LoggerFactory.getLogger(KafkaConnPoolUtils.class);

    static final int CONN_IN_POOL;
    private static volatile KafkaConnPoolUtils instance;
    private static volatile ConcurrentHashMap<String, Producer<String, String>> pool;

    private static volatile long counter;

    static {
        CONN_IN_POOL = Integer.parseInt(PropUtils.getInstance().getProperty("kafka.conn.pool.size"));
        // do not use lazy initialization
        getInstance();
    }

    private KafkaConnPoolUtils() {
        init();
    }

    /**
     * Single instance.
     *
     * @return a single instance of this class
     */
    public static KafkaConnPoolUtils getInstance() {
        if (instance == null)
            synchronized (KafkaConnPoolUtils.class) {
                if (instance == null) {
                    instance = new KafkaConnPoolUtils();
                    initStorage();
                }
            }
        return instance;
    }

    /**
     * Create some new connections into pool, when the size of pool less than MIN_CONN_IN_POOL.
     */
    private static void initStorage() {
        int count = 0;
        int size;
        while ((size = pool.size()) < CONN_IN_POOL && count < CONN_IN_POOL * 2) {
            createNewConnIntoPool(size);
            count++;
        }
        if (pool.size() != CONN_IN_POOL)
            _log.warn("Init Kafka connection pool, size: [{}/{}]", pool.size(), CONN_IN_POOL);
    }

    /**
     * Create a new connection.
     */
    private static void createNewConnIntoPool(int index) {
        Producer<String, String> p = KafkaUtils.createProducer();
        if (p == null) return;
        pool.put(index + "", p);
        _log.debug("Add a new ZKClient Connection into pool...");
        _log.debug("Storage: [{}/{}]", pool.size(), CONN_IN_POOL);
    }

    /**
     * Make a initialization.
     */
    private void init() {
        pool = new ConcurrentHashMap<>();
    }

    /**
     * Get a alive connection from pool.
     *
     * @return a alive zookeeper connection which state is Watcher.Event.KeeperState.SyncConnected
     */
    public Producer<String, String> getConn() {
        long index = (counter %= CONN_IN_POOL);
        counter++;
        _log.debug("Get ZKClient Connection from pool, index: [{} in {}] ...", index, CONN_IN_POOL);
        return pool.get(index + "");
    }
}
