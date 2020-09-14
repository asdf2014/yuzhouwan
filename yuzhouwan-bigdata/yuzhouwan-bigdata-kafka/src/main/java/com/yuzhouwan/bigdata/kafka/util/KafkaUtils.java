package com.yuzhouwan.bigdata.kafka.util;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEvent;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventFactory;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventProducer;
import com.yuzhouwan.bigdata.kafka.util.pc.AvroEventWorkHandler;
import com.yuzhouwan.common.util.PropUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

import static com.yuzhouwan.bigdata.kafka.util.KafkaConnPoolUtils.getPool;
import static com.yuzhouwan.common.util.ExceptionUtils.errorInfo;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Utils
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public final class KafkaUtils {

    private static final Logger _log = LoggerFactory.getLogger(KafkaUtils.class);
    private static final PropUtils p = PropUtils.getInstance();
    private static final String PARTITIONER_CLASS_NAME = KafkaPartitioner.class.getName();
    private static final String KAFKA_TOPIC = p.getProperty("kafka.topic");
    private static final int RING_BUFFER_SIZE = Integer.parseInt(p.getProperty("ringbuffer.size"));
    private static final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private static final Encoder encoder = EncoderFactory.get().binaryEncoder(os, null);
    private static volatile KafkaUtils instance;
    private static volatile AvroEventProducer avroProducer;
    private static HashMap<Class, DatumWriter> datumWriterPool = new HashMap<>();

    private KafkaUtils() {
    }

    public static KafkaUtils getInstance() {
        init();
        return instance;
    }

    private static void init() {
        if (instance == null) synchronized (KafkaUtils.class) {
            if (instance == null) internalInit();
        }
    }

    private static void internalInit() {
        instance = new KafkaUtils();
        disruptor();
    }

    private static void disruptor() {
        AvroEventFactory factory = new AvroEventFactory();
        Disruptor<AvroEvent> disruptor = new Disruptor<>(factory, RING_BUFFER_SIZE,
                Executors.newCachedThreadPool(),
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        Collection<Producer<String, byte[]>> pool = getPool();
        int poolSize = pool.size();
        AvroEventWorkHandler[] avroEventWorkHandlers = new AvroEventWorkHandler[poolSize];
        int count = 0;
        for (Producer<String, byte[]> producer : pool) {
            avroEventWorkHandlers[count] = new AvroEventWorkHandler(producer, KAFKA_TOPIC, count);
            count++;
        }
        disruptor.handleEventsWithWorkerPool(avroEventWorkHandlers);
        disruptor.start();

        RingBuffer<AvroEvent> ringBuffer = disruptor.getRingBuffer();
        avroProducer = new AvroEventProducer(ringBuffer);
    }

    static Producer<String, byte[]> createProducer() {
        Properties props = new Properties();
        try {
//            props.put("zk.connect", p.getProperty("kafka.zk.connect"));   // not need zk in new version
            props.put("key.serializer.class", p.getProperty("kafka.key.serializer.class"));
            props.put("serializer.class", p.getProperty("kafka.serializer.class"));
            props.put("metadata.broker.list", p.getProperty("kafka.metadata.broker.list"));
            props.put("request.required.acks", p.getProperty("kafka.request.required.acks"));
            props.put("producer.type", p.getProperty("kafka.async"));
            props.put("partitioner.class", PARTITIONER_CLASS_NAME);

            props.put("queue.buffering.max.ms", p.getProperty("kafka.queue.buffering.max.ms"));
            props.put("queue.buffering.max.messages", p.getProperty("kafka.queue.buffering.max.messages"));
            props.put("queue.enqueue.timeout.ms", p.getProperty("kafka.queue.enqueue.timeout.ms"));
            // 41,0000,0000 / 24 / 60 / 60 = 47454 / 24 = 1977
            props.put("batch.num.messages", p.getProperty("kafka.batch.num.messages"));
            props.put("send.buffer.bytes", p.getProperty("kafka.send.buffer.bytes"));
//            props.put("compression.type", "lz4");
        } catch (Exception e) {
            _log.error("Connect with kafka failed {}!", e.getMessage());
            throw new RuntimeException(e);
        }
        _log.info("Connect with kafka successfully!");
        return new Producer<>(new ProducerConfig(props));
    }

    public static <T> void save2Kafka(final List<T> objs, Class<T> clazz) {
        for (T obj : objs) sendMessageToKafka(obj, clazz);
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> void sendMessageToKafka(T message, Class<T> clazz) {
        try {
            DatumWriter datumWriter;
            if (datumWriterPool.containsKey(clazz)) datumWriter = datumWriterPool.get(clazz);
            else {
                datumWriter = new SpecificDatumWriter<>(clazz);
                datumWriterPool.put(clazz, datumWriter);
            }
            datumWriter.write(message, encoder);
            encoder.flush();
            avroProducer.product(os.toByteArray());
            os.reset();
        } catch (Exception e) {
            _log.error(errorInfo(e));
        }
    }
}
