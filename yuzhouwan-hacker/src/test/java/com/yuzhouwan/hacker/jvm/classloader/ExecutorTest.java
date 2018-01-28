package com.yuzhouwan.hacker.jvm.classloader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Executor Test
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ExecutorTest {

    @Test
    public void testExecuteV1() {
        ExecutorProxy executor = new ExecutorProxy("v1");
        assertEquals("V1:BenedictJin", executor.execute("BenedictJin"));
    }

    @Test
    public void testExecuteV2() {
        ExecutorProxy executor = new ExecutorProxy("v2");
        assertEquals("V2:BenedictJin", executor.execute("BenedictJin"));
    }
}
