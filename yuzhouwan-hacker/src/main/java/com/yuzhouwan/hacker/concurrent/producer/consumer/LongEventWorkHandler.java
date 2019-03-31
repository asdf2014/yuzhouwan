package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.WorkHandler;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Work Handler
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventWorkHandler implements WorkHandler<LongEvent> {

    private String workerName;

    public LongEventWorkHandler(String workerName) {
        this.workerName = workerName;
    }

    @Override
    public void onEvent(LongEvent event) {
//        System.out.println(workerName + " handle event:" + event);
    }
}
