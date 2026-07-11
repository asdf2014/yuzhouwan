package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：KafkaConnPool Utils Test
 *
 * @author Benedict Jin
 * @since 2016/12/9
 */
public class KafkaConnPoolUtilsTest {

    @Ignore
    @Test
    public void getConnTest() throws Exception {
        PropUtils p = PropUtils.getInstance();
        int kafkaConnPoolSize = Integer.parseInt(p.getProperty("kafka.conn.pool.size"));
        Producer<String, byte[]> conn = KafkaConnPoolUtils.getInstance().getConn();
        String topic = p.getProperty("kafka.topic");
        for (int i = 0, max = 1000000000; i < max; i++) {
            System.out.printf("Sending %s/%s ...%n", i, max);
            Thread.sleep(1000);
            conn.send(new ProducerRecord<>(topic,
                    ("{\"appId\":1,\"attemptId\":\"2\",\"callId\":\"" + i + "\",\"description\":\"yuzhouwan\"}")
                            .getBytes()));
        }
        for (int i = 1; i < 2 * kafkaConnPoolSize; i++) KafkaConnPoolUtils.getInstance().getConn();
    }

    @Test
    public void indexTest() {
        {
            int CONN_INDEX = 0;
            int CONN_IN_POOL = 3;
            long index = (CONN_INDEX %= CONN_IN_POOL);
            CONN_INDEX++;
            assertEquals(1, CONN_INDEX);
            assertEquals(0, index);
        }
        {
            int CONN_INDEX = 0;
            int CONN_IN_POOL = 3;
            long index = (CONN_INDEX += CONN_INDEX % CONN_IN_POOL);
            assertEquals(0, CONN_INDEX);
            assertEquals(0, index);
        }
        {
            int CONN_INDEX = 0;
            int CONN_IN_POOL = 3;
            long index = (CONN_INDEX += CONN_INDEX % CONN_IN_POOL);
            assertEquals(0, CONN_INDEX);
            assertEquals(0, index);
        }
    }
}
