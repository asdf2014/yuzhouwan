package com.yuzhouwan.hacker.algorithms.thread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Semaphore Try
 *
 * @author Benedict Jin
 * @since 2016/9/5
 */
class SemaphoreTry {

    private static final Semaphore semaphore = new Semaphore(3);

    static void semaphore() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " get the semaphore "
                        + " now have " + (3 - semaphore.availablePermits()) + " running...");
                System.out.println(Thread.currentThread().getName() + " will leave...");
                semaphore.release();
                System.out.println("all of the thread is " + (3 - semaphore.availablePermits()));
            });
        }
        executorService.shutdown();
    }
}
