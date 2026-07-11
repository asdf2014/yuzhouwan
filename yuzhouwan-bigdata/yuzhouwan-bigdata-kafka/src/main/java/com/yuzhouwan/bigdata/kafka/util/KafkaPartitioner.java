package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Partitioner
 *
 * @author Benedict Jin
 * @since 2016/12/30
 */
public class KafkaPartitioner implements Partitioner {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPartitioner.class);
    private static final Random r = new Random();

    /**
     * [Note]: the construction is necessary.
     */
    public KafkaPartitioner() {
    }

    /**
     * Load balancer.
     *
     * @param key           the message that sending to kafka hold a key
     * @param numPartitions start with zero
     * @return which index of partition
     */
    public static int getPartition(Object key, int numPartitions) {
        if (numPartitions <= 1) return 0;
        String keyStr;
        if (key == null || StrUtils.isEmpty(keyStr = key.toString()) || keyStr.length() > 1_000)
            return r.nextInt(numPartitions);    // [0, numPartitions)
        try {
            if (StrUtils.isNumber(keyStr)) return (int) Math.abs(Long.parseLong(keyStr) % numPartitions);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
        }
        return Math.abs(key.hashCode() % numPartitions);
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        Integer numPartitions = cluster.partitionCountForTopic(topic);
        return getPartition(key, numPartitions == null ? 0 : numPartitions);
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
