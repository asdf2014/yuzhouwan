package com.yuzhouwan.hacker.concurrent.producer.consumer;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Long Event
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class LongEvent {

    private long value;

    public void set(long value) {
        this.value = value;
    }

    public long get() {
        return this.value;
    }
}
