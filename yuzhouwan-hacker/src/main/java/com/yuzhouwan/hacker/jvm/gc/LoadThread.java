package com.yuzhouwan.hacker.jvm.gc;

import java.text.SimpleDateFormat;
import java.util.AbstractQueue;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Load Thread
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
class LoadThread extends Thread {

    private static final long TIME_ZERO = System.currentTimeMillis();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    @Override
    public void run() {
        AbstractQueue<String> queue = new ArrayBlockingQueue<>(GCTest.countDownSize);
        for (int i = 0, finishedUnit = 0; ; i = i + 1) {
            // Simulate object use to force promotion into OldGen and then GC
            if (queue.size() >= GCTest.countDownSize) {
                for (int j = 0; j < GCTest.eachRemoveSize; j++) {
                    queue.remove();
                }
                finishedUnit++;

                // every 1000 removal is counted as 1 unit.
                if (System.currentTimeMillis() - TIME_ZERO > GCTest.duration * 1000L) {
                    System.exit(0);
                }
                System.out.println(SIMPLE_DATE_FORMAT.format(new Date()) + " finished Units (1K) = " + finishedUnit);
            }
            queue.add(new String(new char[GCTest.referenceSize]).replace('\0', 'a'));
        }
    }
}
