package com.yuzhouwan.hacker.algorithms.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: ReentrantLockFairness
 *
 * @author Benedict Jin
 * @link https://yuzhouwan.com/posts/27328/
 * @since 2016/9/5
 */
class ReentrantLockFairness {

    static Lock FAIR_LOCK = new ReentrantLock(true);
    static Lock UNFAIR_LOCK = new ReentrantLock();

    /**
     * Fairness.
     */
    static class Fairness implements Runnable {
        private final Lock lock;

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
