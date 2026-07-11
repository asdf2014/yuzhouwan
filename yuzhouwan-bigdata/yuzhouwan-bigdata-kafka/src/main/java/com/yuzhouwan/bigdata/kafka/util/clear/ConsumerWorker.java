package com.yuzhouwan.bigdata.kafka.util.clear;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Worker
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class ConsumerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerWorker.class);

    private final KafkaConsumer<byte[], byte[]> consumer;
    private final String topic;
    private final int threadNum;

    public ConsumerWorker(KafkaConsumer<byte[], byte[]> consumer, String topic, int threadNum) {
        this.consumer = consumer;
        this.topic = topic;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        int total = 0, fail = 0, success = 0;
        long start = System.currentTimeMillis();
        try {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<byte[], byte[]> record : records) {
                    try {
                        LOGGER.info("Thread {}: {}", threadNum, new String(record.value(), StandardCharsets.UTF_8));
                        LOGGER.info("partition: {}, offset: {}", record.partition(), record.offset());
                        success++;
                    } catch (Exception e) {
                        LOGGER.error("", e);
                        fail++;
                    }
                    LOGGER.info("Count [fail/success/total]: [{}/{}/{}], Time: {}s", fail, success, ++total,
                            (System.currentTimeMillis() - start) / 1000);
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {
            LOGGER.info("Consumer worker {} is waking up for shutdown", threadNum);
        } finally {
            consumer.close();
        }
    }

    void shutdown() {
        consumer.wakeup();
    }
}
