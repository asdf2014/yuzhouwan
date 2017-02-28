package com.yuzhouwan.hacker.algorithms.array;

import org.junit.Test;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Circular Buffer Test
 *
 * @author Benedict Jin
 * @since 2017/02/28
 */
public class CircularBufferTest {

    @Test
    public void test() {

        CircularBuffer cb = new CircularBuffer(10);
        for (int i = 0; i < 20; i++) {
            cb.buffer(i);
        }
        for (int i : cb.getBuffer()) {
            System.out.println(i);
        }
    }
}
