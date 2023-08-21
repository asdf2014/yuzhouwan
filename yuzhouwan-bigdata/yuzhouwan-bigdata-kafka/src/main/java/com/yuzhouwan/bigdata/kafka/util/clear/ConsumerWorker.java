package com.yuzhouwan.bigdata.kafka.util.clear;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Worker
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class ConsumerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerWorker.class);

    private KafkaStream<byte[], byte[]> kafkaStream;
    private int threadNum;

    public ConsumerWorker(KafkaStream<byte[], byte[]> kafkaStream, int threadNum) {
        this.threadNum = threadNum;
        this.kafkaStream = kafkaStream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> iter = kafkaStream.iterator();
        MessageAndMetadata<byte[], byte[]> msg;
        int total = 0, fail = 0, success = 0;
        long start = System.currentTimeMillis();
        while (iter.hasNext()) {
            try {
                msg = iter.next();
                LOGGER.info("Thread {}: {}", threadNum, new String(msg.message(), StandardCharsets.UTF_8));
                LOGGER.info("partition: {}, offset: {}", msg.partition(), msg.offset());
                success++;
            } catch (Exception e) {
                LOGGER.error("", e);
                fail++;
            }
            LOGGER.info("Count [fail/success/total]: [{}/{}/{}], Time: {}s", fail, success, ++total,
                    (System.currentTimeMillis() - start) / 1000);
        }
    }
}
