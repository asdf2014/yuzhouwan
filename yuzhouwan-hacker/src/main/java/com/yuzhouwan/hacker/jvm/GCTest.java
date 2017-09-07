package com.yuzhouwan.hacker.jvm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractQueue;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：GC Test
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class GCTest {

    private static int threadNum = 1;
    static int duration = 30;               // seconds;  Program will exit after Duration of seconds.

    static int referenceSize = 512;         //1024 * 10;  // each reference object size;
    static int countDownSize = 1000 * 100;
    static int eachRemoveSize = 1000 * 50;  // remove # of elements each time.

    // -Xmx512M -Xms256M -Xloggc:gc.log -XX:+UseG1GC -XX:+PrintGCApplicationStoppedTime
    // -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            duration = Integer.parseInt(args[0]);
            threadNum = Integer.parseInt(args[1]);
        }
        for (int i = 0; i < threadNum; i++) {
            LoadThread thread = new LoadThread();
            thread.start();
        }
    }
}

/**
 * LoadThread.
 */
class LoadThread extends Thread {

    private static long timeZero = System.currentTimeMillis();

    LoadThread() {
    }

    public void run() {
        AbstractQueue<String> q = new ArrayBlockingQueue<>(GCTest.countDownSize);
        char[] srcArray;
        String emptyStr;
        long finishedUnit = 0;
        long prevTime = timeZero;

        for (int i = 0; ; i = i + 1) {
            // Simulate object use to force promotion into OldGen and then GC
            if (q.size() >= GCTest.countDownSize) {
                for (int j = 0; j < GCTest.eachRemoveSize; j++) {
                    q.remove();
                }
                finishedUnit++;

                // every 1000 removal is counted as 1 unit.
                long curTime = System.currentTimeMillis();
                long totalTime = curTime - timeZero;
                if (totalTime > GCTest.duration * 1000) {
                    System.exit(0);
                }
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                System.out.println(ft.format(dNow) + " finished Units (1K) = " + finishedUnit);
            }
            srcArray = new char[GCTest.referenceSize];
            emptyStr = new String(srcArray);
            String str = emptyStr.replace('\0', 'a');
            q.add(str);
        }
    }
}
