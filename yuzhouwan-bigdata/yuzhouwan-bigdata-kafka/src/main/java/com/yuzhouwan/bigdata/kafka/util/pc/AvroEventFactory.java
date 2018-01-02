package com.yuzhouwan.bigdata.kafka.util.pc;

import com.lmax.disruptor.EventFactory;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Avro Event Factory
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class AvroEventFactory implements EventFactory<AvroEvent> {

    @Override
    public AvroEvent newInstance() {
        return new AvroEvent();
    }
}
