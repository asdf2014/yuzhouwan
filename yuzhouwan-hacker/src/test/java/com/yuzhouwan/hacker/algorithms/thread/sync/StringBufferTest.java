package com.yuzhouwan.hacker.algorithms.thread.sync;

import org.junit.Test;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：String Buffer Test
 *
 * @author Benedict Jin
 * @since 2017/9/28
 */
public class StringBufferTest {

    public static void strBufferWithoutSync() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 9999999; i++) {
            buffer.append(i);
            buffer.delete(0, buffer.length() - 1);
        }
    }

    public static void strBufferWithSync() {
        StringBuffer buffer = new StringBuffer();
        synchronized (buffer) {
            for (int i = 0; i < 9999999; i++) {
                buffer.append(i);
                buffer.delete(0, buffer.length() - 1);
            }
        }
    }

    public static void strBuilderWithSync() {
        StringBuilder buffer = new StringBuilder();
        synchronized (buffer) {
            for (int i = 0; i < 9999999; i++) {
                buffer.append(i);
                buffer.delete(0, buffer.length() - 1);
            }
        }
    }

    /*
     * 1.8.0_111
     * StringBuffer without sync: 244
     * StringBuffer with sync:    236
     * StringBuilder with sync:   236
     */
    @Test
    public void test() {
        System.out.println(System.getProperty("java.version"));
        // warm up for jit
        for (int i = 0; i < 10; i++) {
            strBufferWithoutSync();
            strBufferWithSync();
            strBuilderWithSync();
        }

        long start = System.currentTimeMillis();
        strBufferWithoutSync();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("StringBuffer without sync: " + time);

        start = System.currentTimeMillis();
        strBufferWithSync();
        end = System.currentTimeMillis();
        time = end - start;
        System.out.println("StringBuffer with sync: " + time);

        start = System.currentTimeMillis();
        strBuilderWithSync();
        end = System.currentTimeMillis();
        time = end - start;
        System.out.println("StringBuilder with sync: " + time);
    }
}
