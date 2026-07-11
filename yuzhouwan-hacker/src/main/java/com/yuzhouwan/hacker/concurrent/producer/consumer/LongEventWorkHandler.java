package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.EventHandler;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Work Handler
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventWorkHandler implements EventHandler<LongEvent> {

    private final String workerName;

    public LongEventWorkHandler(String workerName) {
        this.workerName = workerName;
    }

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
//        System.out.println(workerName + " handle event:" + event);
    }
}
