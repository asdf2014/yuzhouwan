package com.yuzhouwan.hacker.algorithms.thread.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：FixedThreadPoolTry
 *
 * @author Benedict Jin
 * @since 2015/12/29
 */
public class FixedThreadPoolTry {

    /**
     * ICONST_0
     * ISTORE 1
     * IINC 1 1.
     *
     * @param args
     */
//    volatile static int a = 0;        //volatile 只是保证了可见性，并不能保证 field 在 ++ 这种非原子操作下，不出现并发问题
    public static void main(String[] args) throws InterruptedException {

//        a++;

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("[FixedThread]-%d")
                .setDaemon(true)
                .build();
        ExecutorService executor = Executors.newFixedThreadPool(5, threadFactory);
        Runnable worker;
        for (int i = 0; i < 10; i++) {
            worker = new WorkerThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        for (int count = 1000; count >= 0; count--) {
            if (executor.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                System.out.println("Finished all threads");
                System.exit(0);
            }
        }
    }

    /**
     * pool-1-thread-1 Start. Command = 0
     * Time: 3332
     * pool-1-thread-2 Start. Command = 1
     * Time: 5307
     * pool-1-thread-3 Start. Command = 2
     * Time: 2701
     * pool-1-thread-4 Start. Command = 3
     * Time: 4797
     * pool-1-thread-5 Start. Command = 4
     * Time: 1949
     * pool-1-thread-5 End.
     * pool-1-thread-5 Start. Command = 5
     * Time: 3289
     * pool-1-thread-3 End.
     * pool-1-thread-3 Start. Command = 6
     * Time: 3818
     * pool-1-thread-1 End.
     * pool-1-thread-1 Start. Command = 7
     * Time: 4799
     * pool-1-thread-4 End.
     * pool-1-thread-4 Start. Command = 8
     * Time: 5039
     * pool-1-thread-5 End.
     * pool-1-thread-5 Start. Command = 9
     * Time: 713
     * pool-1-thread-2 End.
     * pool-1-thread-5 End.
     * pool-1-thread-3 End.
     * pool-1-thread-1 End.
     * pool-1-thread-4 End.
     * Finished all threads
     */
}
