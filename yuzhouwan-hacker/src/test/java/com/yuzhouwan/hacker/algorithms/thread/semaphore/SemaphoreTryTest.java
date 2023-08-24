package com.yuzhouwan.hacker.algorithms.thread.semaphore;

import org.junit.Test;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Semaphore Try Tester
 *
 * @author Benedict Jin
 * @since 2016/9/5
 */
public class SemaphoreTryTest {

    @Test
    public void semaphore() {

        /*
         * pool-1-thread-1 get the semaphore  now have 2 running...
         * pool-1-thread-3 get the semaphore  now have 3 running...
         * pool-1-thread-2 get the semaphore  now have 2 running...
         * pool-1-thread-3 will leave...
         * pool-1-thread-1 will leave...
         * all of the thread is 1
         * pool-1-thread-5 get the semaphore  now have 2 running...
         * pool-1-thread-2 will leave...
         * all of the thread is 1
         * pool-1-thread-5 will leave...
         * pool-1-thread-6 get the semaphore  now have 3 running...
         * pool-1-thread-6 will leave...
         * all of the thread is 1
         * pool-1-thread-4 get the semaphore  now have 2 running...
         * pool-1-thread-4 will leave...
         * all of the thread is 0
         * all of the thread is 1
         * pool-1-thread-9 get the semaphore  now have 1 running...
         * all of the thread is 2
         * pool-1-thread-7 get the semaphore  now have 2 running...
         * pool-1-thread-7 will leave...
         * pool-1-thread-9 will leave...
         * all of the thread is 2
         * pool-1-thread-8 get the semaphore  now have 3 running...
         * all of the thread is 2
         * pool-1-thread-10 get the semaphore  now have 3 running...
         * pool-1-thread-10 will leave...
         * pool-1-thread-8 will leave...
         * all of the thread is 1
         * all of the thread is 0
         */
        SemaphoreTry.semaphore();
    }
}
