package com.yuzhouwan.bigdata.kafka.util.clear;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Worker
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class ConsumerWorker implements Runnable {

    private static Logger _log = LoggerFactory.getLogger(ConsumerWorker.class);

    private KafkaStream<byte[], byte[]> kafkaStream;
    private int threadNum;

    public ConsumerWorker(KafkaStream<byte[], byte[]> kafkaStream, int threadNum) {
        this.threadNum = threadNum;
        this.kafkaStream = kafkaStream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> consumerIterator = kafkaStream.iterator();
        while (consumerIterator.hasNext()) {
            try {
                MessageAndMetadata<byte[], byte[]> thisMetadata = consumerIterator.next();
                String jsonStr = new String(thisMetadata.message(), "utf-8");
                _log.info("Thread {}: {}", threadNum, jsonStr);
                _log.info("partition{}, offset: {}", thisMetadata.partition(), thisMetadata.offset());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    _log.error("{}", e);
                }
            } catch (UnsupportedEncodingException e) {
                _log.error("{}", e);
            }
        }
    }
}