package com.yuzhouwan.hacker.jvm.gc;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：GC Test
 *
 * @author Benedict Jin
 * @since 2017/3/16
 */
public class GCTest {

    static int duration = 30;               // seconds;  Program will exit after Duration of seconds.
    static int referenceSize = 512;         // 1024 * 10;  // each reference object size;
    static int countDownSize = 1000 * 100;
    static int eachRemoveSize = 1000 * 50;  // remove # of elements each time.
    private static int threadNum = 1;

    // -Xmx512M -Xms256M -Xloggc:gc.log -XX:+UseG1GC -XX:+PrintGCApplicationStoppedTime
    // -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps
    public static void main(String[] args) {
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
