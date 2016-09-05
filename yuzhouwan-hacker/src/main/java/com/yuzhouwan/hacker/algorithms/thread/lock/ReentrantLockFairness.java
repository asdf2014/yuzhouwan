package com.yuzhouwan.hacker.algorithms.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: ReentrantLockFairness
 *
 * @author Benedict Jin
 * @since 2016/9/5
 */
class ReentrantLockFairness {

    static Lock FAIR_LOCK = new ReentrantLock(true);
    static Lock UNFAIR_LOCK = new ReentrantLock();

    static class Fairness implements Runnable {
        private Lock lock;

        Fairness(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    System.out.print(Thread.currentThread().getName());
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}