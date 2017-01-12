package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.DecimalUtils;
import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.yuzhouwan.common.util.ThreadUtils.buildExecutorService;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Utils
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public class KafkaUtils {

    private static final Logger _log = LoggerFactory.getLogger(KafkaUtils.class);

    private volatile static KafkaUtils instance;
    private volatile static ExecutorService pool;

    private static Integer SEND_KAFKA_FACTOR;

    private static final String SEND_KAFKA_INFOS_BASIC = "Thread:{}, Used Time: {}ms, Size: {}MB, Speed: {}ms/MB";
    private static final String SEND_KAFKA_INFOS_DESCRIBE = "[{}] ".concat(SEND_KAFKA_INFOS_BASIC);
    private static final String PARTITIONER_CLASS_NAME = KafkaPartitioner.class.getName();

    // using for judge key instanceof Number, MUST use atomicInteger instead of volatile
    private static AtomicInteger PRODUCER_INDEX = new AtomicInteger(0);

    static {
        // <number of message> / (<number of partition> * factor>)
        // 31934 / (24 * 100) = 13.3 = 14
        // 4759  / (3 * 100)  = 15.8 = 16
        SEND_KAFKA_FACTOR = Integer.parseInt(PropUtils.getInstance().getProperty("job.send.2.kafka.factor"));
        if (SEND_KAFKA_FACTOR < 10 || SEND_KAFKA_FACTOR > 1000) SEND_KAFKA_FACTOR = 100;
    }

    private KafkaUtils() {
    }

    private static void init() {
        if (instance == null)
            synchronized (KafkaUtils.class) {
                if (instance == null) internalInit();
            }
    }

    private static void internalInit() {
        instance = new KafkaUtils();
        buildPool();
    }

    public static Producer<String, String> createProducer() {
        Properties props = new Properties();
        try {
            PropUtils p = PropUtils.getInstance();
//            props.put("zk.connect", p.getProperty("kafka.zk.connect"));   // not need zk in new version
            props.put("serializer.class", p.getProperty("kafka.serializer.class"));
            props.put("metadata.broker.list", p.getProperty("kafka.metadata.broker.list"));
            props.put("request.required.acks", p.getProperty("kafka.request.required.acks"));
            props.put("partitioner.class", PARTITIONER_CLASS_NAME);
        } catch (Exception e) {
            _log.error("Connect with kafka failed, error: {}!", e.getMessage());
            throw new RuntimeException(e);
        }
        _log.info("Connect with kafka successfully!");
        return new Producer<>(new ProducerConfig(props));
    }

    public static KafkaUtils getInstance() {
        init();
        return instance;
    }

    public void sendMessageToKafka(String message) {
        sendMessageToKafka(message, null);
    }

    public void sendMessageToKafka(String message, String key) {
        Producer<String, String> p;
        if ((p = KafkaConnPoolUtils.getInstance().getConn()) == null) {
            _log.warn("Cannot get Producer in connect pool!");
            return;
        }
        p.send(new KeyedMessage<>(PropUtils.getInstance().getProperty("kafka.topic"),
                StrUtils.isEmpty(key) ? PRODUCER_INDEX + "" : key, message));
        if (StrUtils.isEmpty(key)) if (PRODUCER_INDEX.incrementAndGet() >= Integer.MAX_VALUE) PRODUCER_INDEX.set(0);
    }

    public boolean putPool(Runnable r) {
        if (r == null || pool == null) return false;
        pool.execute(r);
        return true;
    }

    private static void buildPool() {
        String kafkaSendThreadPoolCoreNumber = PropUtils.getInstance().getProperty("kafka.send.thread.pool.core.number");
        if (StrUtils.isEmpty(kafkaSendThreadPoolCoreNumber)) kafkaSendThreadPoolCoreNumber = "10";
        pool = buildExecutorService(Integer.parseInt(kafkaSendThreadPoolCoreNumber), "Kafka", true);
    }

    public static <T> void save2Kafka(final List<T> objs) {
        save2Kafka(objs, false, null);
    }

    public static <T> void save2Kafka(final List<T> objs, final String describe) {
        save2Kafka(objs, false, describe);
    }

    public static <T> void save2Kafka(final List<T> objs, boolean isBalance, final String describe) {
        List<T> copy;
        if (isBalance) {
            copy = new LinkedList<>();
            int len;
            for (int i = 0; i < (len = objs.size()); i++) {
                copy.add(objs.get(i));
                if (i % (KafkaConnPoolUtils.CONN_IN_POOL * SEND_KAFKA_FACTOR) == 0 || i == (len - 1))
                    internalPutPool(copy, describe);
            }
        } else {
            copy = new LinkedList<>(objs);
            internalPutPool(copy, describe);
        }
    }

    private static <T> void internalPutPool(final List<T> copy, final String describe) {
        getInstance().putPool(new Runnable() {
            final List<T> deepCopy = new LinkedList<>(copy);

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                double size = 0;
                String json;
                for (T obj : deepCopy)
                    try {
                        getInstance().sendMessageToKafka(json = obj.toString());
                        size += json.getBytes().length;
                    } catch (Exception e) {
                        _log.error(ExceptionUtils.errorInfo(e));
                    }
                long end = System.currentTimeMillis();
                long period = end - start;
                double dataSize = size / 1024 / 1024;
                if (StrUtils.isEmpty(describe))
                    _log.info(SEND_KAFKA_INFOS_BASIC,
                            Thread.currentThread().getName(), period, DecimalUtils.saveTwoPoint(size / 1024 / 1024),
                            DecimalUtils.saveTwoPoint(period / dataSize));
                else
                    _log.info(SEND_KAFKA_INFOS_DESCRIBE,
                            describe, Thread.currentThread().getName(), period,
                            DecimalUtils.saveTwoPoint(size / 1024 / 1024),
                            DecimalUtils.saveTwoPoint(period / dataSize));
                deepCopy.clear();
            }
        });
        copy.clear();
    }
}