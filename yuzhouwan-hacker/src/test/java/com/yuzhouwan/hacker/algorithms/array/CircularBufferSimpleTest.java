package com.yuzhouwan.hacker.algorithms.array;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Circular Buffer Test
 *
 * @author Benedict Jin
 * @since 2017/02/28
 */
public class CircularBufferSimpleTest {

    @Test
    public void test() {

        CircularBufferSimple cb = new CircularBufferSimple(10);
        for (int i = 0; i < 20; i++) cb.buffer(i);
        assertEquals("[19,10,11,12,13,14,15,16,17,18]", JSON.toJSONString(cb.getBuffer()));
    }
}
