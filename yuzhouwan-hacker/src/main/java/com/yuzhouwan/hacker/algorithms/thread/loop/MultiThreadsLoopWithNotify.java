package com.yuzhouwan.hacker.algorithms.thread.loop;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：thread
 *
 * @author Benedict Jin
 * @since 2016/1/21
 */
public class MultiThreadsLoopWithNotify {

    public static void main(String[] args) {

        LockHolder lockHolder = new LockHolder("go ahead");
        Reporter reporter1 = new Reporter(lockHolder);
//        Reporter reporter2 = new Reporter(lockHolder);
        Leader leader = new Leader(lockHolder);
        Thread t1 = new Thread(reporter1);
//        Thread t2 = new Thread(reporter2);
        Thread t3 = new Thread(leader);

        t1.start();
//        t2.start();
        t3.start();
    }

    /**
     * Leader.
     */
    private record Leader(LockHolder lockHolder) implements Runnable {

        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                count--;
                String threadName = Thread.currentThread().getName();
                try {
                    Thread.sleep(3000);
                    synchronized (lockHolder) {
                        lockHolder.notify();
                        System.out.println("Thread: [".concat(threadName).concat("] sent the message is ")
                          .concat(lockHolder.getHolder()).concat("."));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Reporter.
     */
    private record Reporter(LockHolder lockHolder) implements Runnable {

        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                count--;
                String threadName = Thread.currentThread().getName();
                try {
                    Thread.sleep(1000);
                    synchronized (lockHolder) {
                        System.out.println("Thread: [".concat(threadName).concat("] is waiting the message..."));
                        lockHolder.wait();
                        System.out.println("Thread: [".concat(threadName).concat("] got the message is ")
                          .concat(lockHolder.getHolder()).concat("."));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * LockHolder.
     */
    private static class LockHolder {
        private final String holder;

        public LockHolder(String holder) {
            this.holder = holder;
        }

        public String getHolder() {
            return holder;
        }
    }
}
