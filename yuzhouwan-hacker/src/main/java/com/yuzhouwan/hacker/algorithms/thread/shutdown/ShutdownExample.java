package com.yuzhouwan.hacker.algorithms.thread.shutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Shutdown Example
 *
 * @author Benedict Jin
 * @since 2023/12/10
 */
public class ShutdownExample {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                System.out.println("Task started.");
                Thread.sleep(5000);
                System.out.println("Task completed.");
            } catch (InterruptedException e) {
                System.out.println("Task was interrupted.");
            }
        });

        /*
        ExecutorService shutdown initiated.
        Task started.
        Task completed.
         */
        executor.shutdown();

        /*
        ExecutorService shutdown initiated.
        Task started.
        Task was interrupted.
         */
        // executor.shutdownNow();

        System.out.println("ExecutorService shutdown initiated.");
    }
}
