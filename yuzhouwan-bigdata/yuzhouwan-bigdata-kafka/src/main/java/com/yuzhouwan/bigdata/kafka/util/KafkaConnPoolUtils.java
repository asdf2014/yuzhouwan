package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Connection Pool Utils
 *
 * @author Benedict Jin
 * @since 2016/12/30
 */
public class KafkaConnPoolUtils {

    private static final Logger _log = LoggerFactory.getLogger(KafkaConnPoolUtils.class);

    static int CONN_IN_POOL;
    private static volatile KafkaConnPoolUtils instance;
    private static volatile ConcurrentHashMap<String, Producer<String, String>> pool;

    private static volatile long CONN_INDEX;

    static {
        String connPoolSizeStr;
        CONN_IN_POOL = StrUtils.isEmpty(connPoolSizeStr = PropUtils.getInstance().getProperty("kafka.conn.pool.size")) ?
                1 : Integer.parseInt(connPoolSizeStr);
        if (CONN_IN_POOL <= 0 || CONN_IN_POOL > 1_000) CONN_IN_POOL = 1;
        getInstance();  // do not use lazy initialization
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
        Producer<String, String> p;
        if ((p = KafkaUtils.createProducer()) == null) return;
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
        long index = (CONN_INDEX %= CONN_IN_POOL);
        CONN_INDEX++;
        _log.debug("Get ZKClient Connection from pool, index: [{} in {}] ...", index, CONN_IN_POOL);
        return pool.get(index + "");
    }
}