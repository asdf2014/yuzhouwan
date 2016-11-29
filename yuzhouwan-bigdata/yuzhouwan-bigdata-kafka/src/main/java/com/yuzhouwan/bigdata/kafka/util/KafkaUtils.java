package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

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
    private volatile static Producer<String, String> producer;

    private KafkaUtils() {
    }

    private static void init() {
        if (producer == null)
            synchronized (KafkaUtils.class) {
                if (producer == null) {
                    internalInit();
                }
            }
    }

    private static void internalInit() {
        instance = new KafkaUtils();
        try {
            PropUtils p = PropUtils.getInstance();
            Properties props = new Properties();
            props.put("zk.connect", p.getProperty("kafka.zk.connect"));
            props.put("serializer.class", p.getProperty("kafka.serializer.class"));
            props.put("metadata.broker.list", p.getProperty("kafka.metadata.broker.list"));
            props.put("request.required.acks", p.getProperty("kafka.request.required.acks"));
            producer = new Producer<>(new ProducerConfig(props));
        } catch (Exception e) {
            _log.error("{} ---- Connect with kafka failed!", e.getMessage());
            throw new RuntimeException(e);
        }
        _log.info("Connect with kafka successfully!");
    }

    public static KafkaUtils getInstance() {
        init();
        return instance;
    }

    public void sendMessageToKafka(String message) {
//        _log.debug("Send to Kafka: {}", message);
        producer.send(new KeyedMessage<String, String>(PropUtils.getInstance().getProperty("kafka.topic"), message));
    }

    public void reConnect() {
        closeConnectionWithKafka();
        internalInit();
    }

    public void closeConnectionWithKafka() {
        if (producer != null)
            producer.close();
    }
}