package com.yuzhouwan.hacker.jvm.classloader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Executor Test
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ExecutorTest {

    /*
    V1 Executor
        Loading class: com.yuzhouwan.hacker.jvm.classloader.ExecutorV1
        Loading class: com.yuzhouwan.hacker.jvm.classloader.AbstractExecutor
        Loading class: com.yuzhouwan.hacker.jvm.classloader.Executor
        Loading class: java.lang.Object
        Loading class: com.yuzhouwan.hacker.jvm.classloader.Handler
        Loading class: com.yuzhouwan.hacker.jvm.classloader.AbstractExecutor$1
        Loading class: com.yuzhouwan.hacker.jvm.classloader.ExecutorV1$1
        Loading class: java.lang.String
        Loading class: java.lang.Thread
        Loading class: java.lang.Class
        Loading class: java.lang.StringBuilder

    V2 Executor
        Loading class: com.yuzhouwan.hacker.jvm.classloader.ExecutorV2
        Loading class: com.yuzhouwan.hacker.jvm.classloader.AbstractExecutor
        Loading class: com.yuzhouwan.hacker.jvm.classloader.Executor
        Loading class: java.lang.Object
        Loading class: com.yuzhouwan.hacker.jvm.classloader.Handler
        Loading class: com.yuzhouwan.hacker.jvm.classloader.AbstractExecutor$1
        Loading class: com.yuzhouwan.hacker.jvm.classloader.ExecutorV2$1
        Loading class: java.lang.String
        Loading class: java.lang.Thread
        Loading class: java.lang.Class
        Loading class: java.lang.StringBuilder
     */
    @Test
    public void testExecute() {
        String name = "BenedictJin";
        {
            Executor executor = new ExecutorProxy("v1");
            System.out.println("V1 Executor");
            assertEquals("V1:".concat(name), executor.execute(name));
        }
        {
            Executor executor = new ExecutorProxy("v2");
            System.out.println("V2 Executor");
            assertEquals("V2:".concat(name), executor.execute(name));
        }
    }
}
