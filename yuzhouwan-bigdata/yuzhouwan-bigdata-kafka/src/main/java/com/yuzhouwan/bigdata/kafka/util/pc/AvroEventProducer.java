package com.yuzhouwan.bigdata.kafka.util.pc;

import com.lmax.disruptor.RingBuffer;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Avro Event Producer
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class AvroEventProducer {

    private final RingBuffer<AvroEvent> ringBuffer;

    public AvroEventProducer(RingBuffer<AvroEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void product(byte[] bb) {
        long sequence = ringBuffer.next();
        try {
            AvroEvent event = ringBuffer.get(sequence);
            event.setValue(bb);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
