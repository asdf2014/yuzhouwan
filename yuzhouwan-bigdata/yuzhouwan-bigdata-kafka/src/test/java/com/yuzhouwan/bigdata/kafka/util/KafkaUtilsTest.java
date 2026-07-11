package com.yuzhouwan.bigdata.kafka.util;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEvent;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventFactory;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventProducer;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventWorkHandler;
import com.yuzhouwan.common.util.DecimalUtils;
import org.apache.kafka.clients.producer.Producer;

import java.nio.ByteBuffer;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public class KafkaUtilsTest {

    private static final int BUFFER_SIZE = 1024 * 1024 * 16;

    public static void main(String[] args) {

        AvroEventFactory factory = new AvroEventFactory();
        Disruptor<AvroEvent> disruptor = new Disruptor<>(factory, BUFFER_SIZE, DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI, new BlockingWaitStrategy());
        Producer<String, byte[]> kafkaConn = KafkaConnPoolUtils.getPool().iterator().next();
        disruptor.handleEventsWith(new AvroEventWorkHandler(kafkaConn, "topic", 1)/*,...*/);
        disruptor.start();

        RingBuffer<AvroEvent> ringBuffer = disruptor.getRingBuffer();
        AvroEventProducer producer = new AvroEventProducer(ringBuffer);
        AvroEventProducer producer2 = new AvroEventProducer(ringBuffer);
        AvroEventProducer producer3 = new AvroEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        long start = System.currentTimeMillis();
        long index;
        for (long l = 0; l < 100_0000; l++) {
            bb.putLong(0, l);
            index = l % 3;
            if (index == 0) producer.product(getByteBuffer(bb));
            else if (index == 1) producer2.product(getByteBuffer(bb));
            else producer3.product(getByteBuffer(bb));
        }
        System.out.printf("Time: %dms%n", System.currentTimeMillis() - start);        // Time: 98ms
        disruptor.shutdown();
        System.exit(0);
    }

    private static byte[] getByteBuffer(ByteBuffer bb) {
        return DecimalUtils.byteBuffer2byteArray(bb);
    }
}