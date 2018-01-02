package com.yuzhouwan.hacker.algorithms.thread.lock;

import org.junit.Test;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: ReentrantLockFairness Tester
 *
 * @author Benedict Jin
 * @since 2016/9/5
 */
public class ReentrantLockFairnessTest {

    /**
     * 很少会连续获取到锁
     *
     * 0 0 0 0 0 1 2 4 3 1 2 4 3 1 2 4 3 1 2 4 3 1 2 4 3
     * 0 1 0 1 0 1 0 1 4 0 1 4 4 4 4 2 2 2 2 2 3 3 3 3 3
     * 1 0 2 4 3 1 0 2 4 3 1 0 2 4 3 1 0 2 4 3 1 0 2 4 3
     */
    @Test
    public void fair() throws Exception {
        Thread thread;
        for (int i = 0; i < 5; i++) {
            thread = new Thread(new ReentrantLockFairness.Fairness(ReentrantLockFairness.FAIR_LOCK));
            thread.setName(i + " ");
            thread.start();
        }
        Thread.sleep(1000);
    }

    /**
     * 相比公平锁，非公平锁 能经常性地连续获取到锁
     *
     * 0 0 0 0 0 3 3 3 3 3 1 1 1 1 1 2 2 2 2 2 4 4 4 4 4
     * 0 0 2 2 2 2 2 4 4 4 4 4 1 1 1 1 1 0 0 0 3 3 3 3 3
     * 1 1 1 1 1 4 4 4 4 4 0 0 0 0 0 2 2 2 2 2 3 3 3 3 3
     */
    @Test
    public void unfair() throws Exception {
        Thread thread;
        for (int i = 0; i < 5; i++) {
            thread = new Thread(new ReentrantLockFairness.Fairness(ReentrantLockFairness.UNFAIR_LOCK));
            thread.setName(i + " ");
            thread.start();
        }
        Thread.sleep(1000);
    }
}
