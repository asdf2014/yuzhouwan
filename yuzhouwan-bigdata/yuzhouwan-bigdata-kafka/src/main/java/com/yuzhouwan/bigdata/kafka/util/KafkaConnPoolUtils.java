package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static com.yuzhouwan.bigdata.kafka.util.KafkaUtils.createProducer;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Connection Pool Utils
 *
 * @author Benedict Jin
 * @since 2016/12/30
 */
public final class KafkaConnPoolUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConnPoolUtils.class);

    private static int CONN_IN_POOL;
    private static volatile long CONN_INDEX;
    private static volatile KafkaConnPoolUtils instance;
    private static volatile ConcurrentHashMap<String, Producer<String, byte[]>> pool;

    static {
        String connPoolSizeStr = PropUtils.getInstance().getProperty("kafka.conn.2.CONN_IN_POOL");
        CONN_IN_POOL = StrUtils.isEmpty(connPoolSizeStr) ? 3 : Integer.parseInt(connPoolSizeStr);
        if (CONN_IN_POOL <= 0 || CONN_IN_POOL > 1_000) CONN_IN_POOL = 3;
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
        KafkaConnPoolUtils tmp = instance;
        if (tmp == null) synchronized (KafkaConnPoolUtils.class) {
            tmp = instance;
            if (tmp == null) {
                tmp = new KafkaConnPoolUtils();
                initStorage();
                instance = tmp;
            }
        }
        return tmp;
    }

    /**
     * Create some new connections into pool, when the size of pool less than MIN_CONN_IN_POOL.
     */
    private static void initStorage() {
        int size, count = 0;
        while ((size = pool.size()) < CONN_IN_POOL && count < CONN_IN_POOL * 2) {
            createNewConnIntoPool(size);
            count++;
        }
        if (pool.size() != CONN_IN_POOL)
            LOGGER.warn("Init Kafka connection pool, size: [{}/{}]", pool.size(), CONN_IN_POOL);
    }

    /**
     * Create a new connection.
     */
    private static void createNewConnIntoPool(int index) {
        Producer<String, byte[]> p;
        if ((p = createProducer()) == null) return;
        pool.put(index + "", p);
        LOGGER.debug("Add a new Kafka Connection into pool...");
        LOGGER.debug("Storage: [{}/{}]", pool.size(), CONN_IN_POOL);
    }

    public static Collection<Producer<String, byte[]>> getPool() {
        return pool.values();
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
    public Producer<String, byte[]> getConn() {
        long index = (CONN_INDEX %= CONN_IN_POOL);
        CONN_INDEX++;
        LOGGER.debug("Get Kafka Connection from pool, index: [{} in {}] ...", index + 1, CONN_IN_POOL);
        return pool.get(index + "");
    }
}
