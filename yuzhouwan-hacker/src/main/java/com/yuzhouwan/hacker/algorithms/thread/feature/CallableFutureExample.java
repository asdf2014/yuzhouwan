package com.yuzhouwan.hacker.algorithms.thread.feature;

import com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: CallableFuture Example
 *
 * @author Benedict Jin
 * @since 2016/7/28
 */
public class CallableFutureExample {

    private static final Logger _log = LoggerFactory.getLogger(ReadWriteLockExample.class);

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            Future<String> future = executorService.submit(() -> {
                        Thread.sleep(5);
                        return "yuzhouwan|" + taskId;
                    }
            );
            _log.info("waiting...");
            try {
                _log.debug(future.get());
            } catch (InterruptedException | ExecutionException e) {
                _log.error("", e);
            }
        }

        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService2);
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            completionService.submit(() -> {
                        Thread.sleep(new Random().nextInt(10));
                        return taskId;
                    }
            );
        }
        _log.info("All thread were submitted into pool...");
        for (int i = 0; i < 10; i++) {
            try {
                _log.debug("{}", completionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                _log.error("", e);
            }
        }
        executorService.shutdown();
        executorService2.shutdown();

        /*
         * Future:              need be waiting for thread finished.
         * CompletionService:   get those results of threads by the finish time of every single thread.
         *
         * waiting...
         * yuzhouwan|0
         * waiting...
         * yuzhouwan|1
         * waiting...
         * yuzhouwan|2
         * waiting...
         * yuzhouwan|3
         * waiting...
         * yuzhouwan|4
         *
         * All thread were submitted into pool...
         * 0
         * 1
         * 3
         * 7
         * 5
         * 8
         * 4
         * 2
         * 6
         * 9
         */
    }
}
