package com.yuzhouwan.bigdata.kafka.util.clear;

import com.yuzhouwan.common.util.PropUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Group
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class ConsumerGroup {

    private static final PropUtils p = PropUtils.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerGroup.class);
    private final String topic;
    private final List<ConsumerWorker> workers = new ArrayList<>();
    private ExecutorService executor;

    public ConsumerGroup() {
        this.topic = p.getProperty("kafka.clear.topic");
    }

    public static void main(String[] args) throws InterruptedException {
        ConsumerGroup consumerGroup = new ConsumerGroup();
        consumerGroup.run(Integer.parseInt(p.getProperty("kafka.clear.job.thread.num")));

        Thread.sleep(Integer.MAX_VALUE);
        consumerGroup.shutdown();
    }

    private void shutdown() {
        for (ConsumerWorker worker : workers) worker.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (executor != null && !executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                LOGGER.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            LOGGER.info("Interrupted during shutdown, exiting uncleanly");
        }
    }

    private void run(int threadNum) {
        executor = Executors.newFixedThreadPool(threadNum);
        for (int threadNumber = 0; threadNumber < threadNum; threadNumber++) {
            ConsumerWorker worker = new ConsumerWorker(createConsumer(), topic, threadNumber);
            workers.add(worker);
            executor.submit(worker);
        }
        LOGGER.info("the consumer worker size is {}", workers.size());
    }

    private KafkaConsumer<byte[], byte[]> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", p.getProperty("kafka.clear.bootstrap.servers"));
        props.put("group.id", p.getProperty("kafka.clear.group.id"));
        props.put("enable.auto.commit", p.getProperty("kafka.clear.enable.auto.commit"));
        props.put("auto.commit.interval.ms", p.getProperty("kafka.clear.auto.commit.interval.ms"));
        props.put("auto.offset.reset", p.getProperty("kafka.clear.auto.offset.reset"));
        props.put("session.timeout.ms", p.getProperty("kafka.clear.session.timeout.ms"));
        props.put("key.deserializer", ByteArrayDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
