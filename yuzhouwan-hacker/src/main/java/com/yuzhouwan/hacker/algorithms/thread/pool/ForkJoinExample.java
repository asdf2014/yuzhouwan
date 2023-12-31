package com.yuzhouwan.hacker.algorithms.thread.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Fork Join Example
 *
 * @author Benedict Jin
 * @since 2018/7/11
 */
public class ForkJoinExample {

    public static void main(String[] args) {

        ForkJoinPool.commonPool().getParallelism();
        ForkJoinPool pool = new ForkJoinPool();
        new Thread() {
            {
                this.setName("printHelloWorld");
                this.start();
            }

            @Override
            public void run() {
                pool.submit(() -> System.out.println("Hello,world first"));

                ForkJoinTask<String> task = pool.submit(() -> "Hello,world second");
                // 输出 Hello,world（永远不会输出，也不会报异常， 所以这是个bug）
                try {
                    System.out.println(task.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        new Thread() {
            {
                this.setName("shutdownPool");
                this.start();
            }

            @Override
            public void run() {
                pool.shutdown();
            }
        };
    }
}
