package com.yuzhouwan.hacker.algorithms.thread.loop;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：thread
 *
 * @author Benedict Jin
 * @since 2016/1/21
 */
public class MultiThreadsLoopWithNotify2 {

    public static void main(String[] args) {

        LockHolder lockHolder = new LockHolder(0);
        int threadSize = 3;
        Reporter reporter1 = new Reporter(lockHolder, 0, threadSize);
        Reporter reporter2 = new Reporter(lockHolder, 1, threadSize);
        Reporter reporter3 = new Reporter(lockHolder, 2, threadSize);
        Thread t1 = new Thread(reporter1);
        Thread t2 = new Thread(reporter2);
        Thread t3 = new Thread(reporter3);

        t1.start();
        t2.start();
        t3.start();
    }
}

/**
 * Reporter.
 */
class Reporter implements Runnable {

    private final LockHolder lockHolder;
    private int number;
    private int threadSize;

    Reporter(LockHolder lockHolder, int number, int threadSize) {
        this.lockHolder = lockHolder;
        this.number = number;
        this.threadSize = threadSize;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            int count = 0;
            while (count < 100) {
                Thread.sleep(1000);
                synchronized (lockHolder) {
                    if (lockHolder.getHolder() == number) {
                        System.out.println("Thread: [".concat(threadName).concat("]..."));
                        lockHolder.setHolder((number + 1) % threadSize);
                    } else {
                        lockHolder.wait(100);
                    }
                }
                count++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * LockHolder.
 */
class LockHolder {
    private volatile int holder;

    LockHolder(int holder) {
        this.holder = holder;
    }

    int getHolder() {
        return holder;
    }

    void setHolder(int holder) {
        this.holder = holder;
    }
}
