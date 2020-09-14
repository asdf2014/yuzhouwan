package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Producer
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void product(ByteBuffer bb) {
        long sequence = ringBuffer.next();
        try {
            LongEvent event = ringBuffer.get(sequence);
            event.set(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
