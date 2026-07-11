package com.yuzhouwan.bigdata.kafka.util.pc;

import com.lmax.disruptor.EventHandler;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Avro Event Work Handler
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class AvroEventWorkHandler implements EventHandler<AvroEvent> {

    private final Producer<String, byte[]> producer;
    private final String topic;
    private final String partition;

    public AvroEventWorkHandler(Producer<String, byte[]> producer, String topic, int partition) {
        this.producer = producer;
        this.topic = topic;
        this.partition = partition + "";
    }

    @Override
    public void onEvent(AvroEvent event, long sequence, boolean endOfBatch) {
        producer.send(new KeyedMessage<>(topic, partition, event.getValue()));
    }
}
