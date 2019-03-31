package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.EventFactory;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Factory
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
