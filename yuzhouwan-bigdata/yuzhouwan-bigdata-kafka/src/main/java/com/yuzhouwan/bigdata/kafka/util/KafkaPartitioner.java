package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.StrUtils;
import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Partitioner
 *
 * @author Benedict Jin
 * @since 2016/12/30
 */
public class KafkaPartitioner implements Partitioner {

    private static final Logger _log = LoggerFactory.getLogger(KafkaPartitioner.class);
    private static final Random r = new Random();

    /**
     * [Note]: the construction is necessary.
     */
    public KafkaPartitioner(VerifiableProperties vp) {
    }

    @Override
    public int partition(Object key, int numPartitions) {
        return getPartition(key, numPartitions);
    }

    /**
     * Load balancer
     *
     * @param key           the message that sending to kafka hold a key
     * @param numPartitions start with zero
     * @return which index of partition
     */
    public static int getPartition(Object key, int numPartitions) {
        if (numPartitions <= 1) return 0;
        String keyStr;
        if (key == null || StrUtils.isEmpty(keyStr = key.toString()) || keyStr.length() > 1_000)
            return r.nextInt(numPartitions);
        try {
            if (StrUtils.isNumber(keyStr)) return (int) Math.abs(Long.parseLong(keyStr) % numPartitions);
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
        }
        return Math.abs(key.hashCode() % numPartitions);
    }
}