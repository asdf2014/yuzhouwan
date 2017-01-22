package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public class KafkaUtilsTest {

    //    @Test
    public void testSendMessage() throws Exception {

        KafkaUtils k = KafkaUtils.getInstance();
        k.sendMessageToKafka("yuzhouwan", PropUtils.getInstance().getProperty("kafka.topic"));
    }
}