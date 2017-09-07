package com.yuzhouwan.hacker.concurrent.producer.consumer;

import com.lmax.disruptor.EventHandler;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Long Event Handler
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
//        System.out.println("consumer:" + Thread.currentThread().getName() +
//                " Event: value=" + event.get() + ",sequence=" + sequence +
//                ",endOfBatch=" + endOfBatch);
    }
}
