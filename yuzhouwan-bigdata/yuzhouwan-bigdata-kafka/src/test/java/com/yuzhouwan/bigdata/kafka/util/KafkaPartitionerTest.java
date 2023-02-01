package com.yuzhouwan.bigdata.kafka.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Partitioner Test
 *
 * @author Benedict Jin
 * @since 2015/12/8
 */
public class KafkaPartitionerTest {

    @Test
    public void testSendMessage() {
        assertEquals(0, KafkaPartitioner.getPartition(null, 0));
        assertEquals(0, KafkaPartitioner.getPartition(null, 1));
        assertTrue(KafkaPartitioner.getPartition(null, 2) < 2);
        assertTrue(KafkaPartitioner.getPartition("1", 2) < 2);
        assertEquals(11, KafkaPartitioner.getPartition("11", 24));
        assertEquals(0, KafkaPartitioner.getPartition("24", 24));
    }
}
