package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Main
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventMain {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Executor executor = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024 * 1024 * 16;
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory,
                bufferSize, executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.handleEventsWithWorkerPool(new LongEventWorkHandler("worker-1"),
                new LongEventWorkHandler("worker-2"),
                new LongEventWorkHandler("worker-3"));

        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
//        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
//        LongEventProducerWithTranslator producer2 = new LongEventProducerWithTranslator(ringBuffer);
//        LongEventProducerWithTranslator producer3 = new LongEventProducerWithTranslator(ringBuffer);
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducer producer2 = new LongEventProducer(ringBuffer);
        LongEventProducer producer3 = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        long start = System.currentTimeMillis();
        long index;
        for (long l = 0; l < 100_0000; l++) {
            bb.putLong(0, l);
            index = l % 3;
            if (index == 0) producer.product(bb);
            else if (index == 1) producer2.product(bb);
            else producer3.product(bb);
//            Thread.sleep(100);
        }
        System.out.println(String.format("Time: %dms", System.currentTimeMillis() - start));        // Time: 98ms
        disruptor.shutdown();
        System.exit(0);
    }
}
