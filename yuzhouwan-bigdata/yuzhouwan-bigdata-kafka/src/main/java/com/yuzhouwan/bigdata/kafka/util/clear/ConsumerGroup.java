package com.yuzhouwan.bigdata.kafka.util.clear;

import com.yuzhouwan.common.util.PropUtils;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static kafka.consumer.Consumer.createJavaConsumerConnector;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Consumer Group
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class ConsumerGroup {

    private static final PropUtils p = PropUtils.getInstance();
    private static Logger _log = LoggerFactory.getLogger(ConsumerGroup.class);
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public ConsumerGroup() {
        this.consumer = createJavaConsumerConnector(createConsumerConfig());
        this.topic = p.getProperty("kafka.clear.topic");
    }

    public static void main(String[] args) throws InterruptedException {
        ConsumerGroup consumerGroup = new ConsumerGroup();
        consumerGroup.run(Integer.parseInt(p.getProperty("kafka.clear.job.thread.num")));

        Thread.sleep(Integer.MAX_VALUE);
        consumerGroup.shutdown();
    }

    private void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            assert executor != null;
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                _log.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            _log.info("Interrupted during shutdown, exiting uncleanly");
        }
    }

    private void run(int threadNum) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, threadNum);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        executor = Executors.newFixedThreadPool(threadNum);

        int threadNumber = 0;
        _log.info("the streams size is {}", streams.size());
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(new ConsumerWorker(stream, threadNumber));
            consumer.commitOffsets();
            threadNumber++;
        }
    }

    private ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", p.getProperty("kafka.clear.zookeeper.connect"));
        props.put("group.id", p.getProperty("kafka.clear.group.id"));
        props.put("zookeeper.session.timeout.ms", p.getProperty("kafka.clear.zookeeper.session.timeout.ms"));
        props.put("zookeeper.sync.time.ms", p.getProperty("kafka.clear.zookeeper.sync.time.ms"));
        props.put("auto.commit.interval.ms", p.getProperty("kafka.clear.auto.commit.interval.ms"));
        props.put("auto.offset.reset", p.getProperty("kafka.clear.auto.offset.reset"));
        props.put("rebalance.max.retries", p.getProperty("kafka.clear.rebalance.max.retries"));
        props.put("rebalance.backoff.ms", p.getProperty("kafka.clear.rebalance.backoff.ms"));
        return new ConsumerConfig(props);
    }
}
