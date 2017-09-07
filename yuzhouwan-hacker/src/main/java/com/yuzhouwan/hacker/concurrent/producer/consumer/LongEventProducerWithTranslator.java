package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Producer with Translator
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventProducerWithTranslator {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
            (event, sequence, bb) -> event.set(bb.getLong(0));

    public void product(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
