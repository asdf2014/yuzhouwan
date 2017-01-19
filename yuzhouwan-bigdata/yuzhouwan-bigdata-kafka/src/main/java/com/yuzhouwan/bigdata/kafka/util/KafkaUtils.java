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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
    private static final PropUtils p = PropUtils.getInstance();
    private static final String PARTITIONER_CLASS_NAME = KafkaPartitioner.class.getName();

    private volatile static KafkaUtils instance;
    private volatile static CompletionService<Boolean> pool;

    private static Integer SEND_KAFKA_BLOCK_SIZE;
    private static Integer SEND_KAFKA_BLOCK_SIZE_MIN;
    private static Integer SEND_KAFKA_RUNNABLE_TIMEOUT_MILLISECOND;

    private static final String SEND_KAFKA_INFOS_BASIC = "Thread:{}, Used Time: {}ms, Size: {}MB, Speed: {}ms/MB";
    private static final String SEND_KAFKA_INFOS_DESCRIBE = "[{}] ".concat(SEND_KAFKA_INFOS_BASIC);

    // using for judge key instanceof Number, MUST use atomicInteger instead of volatile
    private static AtomicInteger PRODUCER_INDEX = new AtomicInteger(0);

    static {
        // <number of message> / (<number of partition> * factor>)
        // 31934 / (24 * 100) = 13.3 = 14
        // 4759  / (3 * 100)  = 15.8 = 16
        Integer SEND_KAFKA_FACTOR = Integer.parseInt(p.getProperty("job.send.2.kafka.factor"));
        if (SEND_KAFKA_FACTOR < 10 || SEND_KAFKA_FACTOR > 100_0000) SEND_KAFKA_FACTOR = 100;
        SEND_KAFKA_BLOCK_SIZE = KafkaConnPoolUtils.CONN_IN_POOL * SEND_KAFKA_FACTOR;
        SEND_KAFKA_BLOCK_SIZE_MIN = SEND_KAFKA_BLOCK_SIZE / 10;
        SEND_KAFKA_RUNNABLE_TIMEOUT_MILLISECOND = Integer.parseInt(p.getProperty("job.send.2.kafka.runnable.timeout.millisecond"));
        if (SEND_KAFKA_FACTOR < 0) SEND_KAFKA_RUNNABLE_TIMEOUT_MILLISECOND = 60000;
    }

    private KafkaUtils() {
    }

    public static KafkaUtils getInstance() {
        init();
        return instance;
    }

    private static void init() {
        if (instance == null) synchronized (KafkaUtils.class) {
            if (instance == null) internalInit();
        }
    }

    private static void internalInit() {
        instance = new KafkaUtils();
        buildPool();
    }

    private static void buildPool() {
        String kafkaSendThreadPoolCoreNumber = p.getProperty("kafka.send.thread.pool.core.number");
        String jobPeriodMillisecond = p.getProperty("job.period.millisecond");
        if (StrUtils.isEmpty(kafkaSendThreadPoolCoreNumber)) kafkaSendThreadPoolCoreNumber = "10";
        if (StrUtils.isEmpty(jobPeriodMillisecond)) jobPeriodMillisecond = "60000";
        Integer poolCore = Integer.parseInt(kafkaSendThreadPoolCoreNumber);
        pool = new ExecutorCompletionService<>(buildExecutorService(poolCore, poolCore * 2,
                Long.parseLong(jobPeriodMillisecond), "Kafka", true));
    }

    static Producer<String, String> createProducer() {
        Properties props = new Properties();
        try {
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

    public boolean putPool(Runnable r) {
        if (r == null || pool == null) return false;
        Future<Boolean> f = null;
        try {
            return (f = pool.submit(r, true)).get(SEND_KAFKA_RUNNABLE_TIMEOUT_MILLISECOND, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            _log.warn(ExceptionUtils.errorInfo(e));
            return false;
        } finally {
            if (f != null) f.cancel(true);
        }
    }

    public void sendMessageToKafka(String message, final String topic) {
        sendMessageToKafka(message, topic, null);
    }

    public void sendMessageToKafka(String message, final String topic, String key) {
        Producer<String, String> producer;
        if ((producer = KafkaConnPoolUtils.getInstance().getConn()) == null) {
            _log.warn("Cannot get Producer in connect pool!");
            return;
        }
        producer.send(new KeyedMessage<>(topic, StrUtils.isEmpty(key) ? PRODUCER_INDEX + "" : key, message));
        if (StrUtils.isEmpty(key)) if (PRODUCER_INDEX.incrementAndGet() >= Integer.MAX_VALUE) PRODUCER_INDEX.set(0);
    }

    public void sendMessageToKafka(List<String> message, final String topic) {
        sendMessageToKafka(message, topic, null);
    }

    public void sendMessageToKafka(List<String> message, final String topic, String key) {
        Producer<String, String> producer;
        if ((producer = KafkaConnPoolUtils.getInstance().getConn()) == null) {
            _log.warn("Cannot get Producer in connect pool!");
            return;
        }
        List<KeyedMessage<String, String>> keyedMessages = new ArrayList<>();
        for (String msg : message) {
            keyedMessages.add(new KeyedMessage<>(topic, StrUtils.isEmpty(key) ? PRODUCER_INDEX + "" : key, msg));
            if (StrUtils.isEmpty(key)) if (PRODUCER_INDEX.incrementAndGet() >= Integer.MAX_VALUE) PRODUCER_INDEX.set(0);
        }
        producer.send(keyedMessages);
    }

    public static <T> void save2Kafka(final List<T> objs, final String topic) {
        save2Kafka(objs, false, topic, null);
    }

    public static <T> void save2Kafka(final List<T> objs, final String topic, final String describe) {
        save2Kafka(objs, false, topic, describe);
    }

    public static <T> void save2Kafka(final List<T> objs, boolean isBalance, final String topic, final String describe) {
        if (isBalance) {
            int len, next;
            List<T> copy;
            for (int i = 0; i < (len = objs.size()); i = next) {
                if ((next = (i + SEND_KAFKA_BLOCK_SIZE)) < len && ((len - next) > SEND_KAFKA_BLOCK_SIZE_MIN))
                    copy = objs.subList(i, next);
                else copy = objs.subList(i, len);
                internalPutPool(copy, topic, describe);
            }
        } else internalPutPool(objs, topic, describe);
    }

    private static <T> void internalPutPool(final List<T> copy, final String topic) {
        internalPutPool(copy, topic, null);
    }

    private static <T> void internalPutPool(final List<T> copy, final String topic, final String describe) {
        getInstance().putPool(() -> {
            long start = System.currentTimeMillis();
            double size = 0;
            String json;
            List<String> deepCopy = new LinkedList<>();
            for (T obj : copy)
                try {
                    deepCopy.add(json = obj.toString());
                    size += json.getBytes().length;
                } catch (Exception e) {
                    _log.error(ExceptionUtils.errorInfo(e));
                }
            getInstance().sendMessageToKafka(deepCopy, topic);
            long end = System.currentTimeMillis();
            long period = end - start;
            double dataSize = size / 1024 / 1024;
            if (StrUtils.isEmpty(describe)) _log.info(SEND_KAFKA_INFOS_BASIC, Thread.currentThread().getName(),
                    period, DecimalUtils.saveTwoPoint(dataSize), DecimalUtils.saveTwoPoint(period / dataSize));
            else _log.info(SEND_KAFKA_INFOS_DESCRIBE, describe, Thread.currentThread().getName(), period,
                    DecimalUtils.saveTwoPoint(dataSize), DecimalUtils.saveTwoPoint(period / dataSize));
            deepCopy.clear();
        });
        // [note]: clear the result of sublist method will bring on "structurally modified" issues
        // copy.clear();
    }
}