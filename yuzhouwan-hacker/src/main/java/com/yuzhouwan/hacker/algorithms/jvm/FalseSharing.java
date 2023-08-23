package com.yuzhouwan.hacker.algorithms.jvm;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: False Sharing
 * See <a href="http://ifeve.com/falsesharing/">http://ifeve.com/falsesharing/</a>
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public final class FalseSharing implements Runnable {

    public static final int NUM_THREADS = 4; // change
    public static final long ITERATIONS = 500L * 1000L * 1000L;
    private static final VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    private final int arrayIndex;

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    /**
     * Volatile Long.
     */
    public static final class VolatileLong {
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6; // comment out
    }
}
