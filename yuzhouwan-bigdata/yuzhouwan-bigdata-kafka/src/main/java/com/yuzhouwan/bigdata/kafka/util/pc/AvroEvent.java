package com.yuzhouwan.bigdata.kafka.util.pc;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Avro Event
 *
 * @author Benedict Jin
 * @since 2017/3/17
 */
public class AvroEvent {

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
