package com.yuzhouwan.hacker.algorithms.thread.feature;

import com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Copyright @ 2016 yuzhouwan.com
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
                _log.error("error: {}", e.getMessage());
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
                _log.error("error: {}", e.getMessage());
            }
        }
        executorService.shutdown();
        executorService2.shutdown();

        /**
         * Future:              need be waiting for thread finished.
         * CompletionService:   get those results of threads by the finish time of every single thread.
         *
         * 2016-07-28 14:01:56.251 | INFO | waiting... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:31
         * 2016-07-28 14:01:56.255 | INFO | yuzhouwan|0 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:33
         * 2016-07-28 14:01:56.255 | INFO | waiting... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:31
         * 2016-07-28 14:01:56.260 | INFO | yuzhouwan|1 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:33
         * 2016-07-28 14:01:56.260 | INFO | waiting... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:31
         * 2016-07-28 14:01:56.265 | INFO | yuzhouwan|2 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:33
         * 2016-07-28 14:01:56.265 | INFO | waiting... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:31
         * 2016-07-28 14:01:56.270 | INFO | yuzhouwan|3 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:33
         * 2016-07-28 14:01:56.270 | INFO | waiting... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:31
         * 2016-07-28 14:01:56.275 | INFO | yuzhouwan|4 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:33
         *
         * 2016-07-28 14:01:56.277 | INFO | All thread were submitted into pool... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:49
         * 2016-07-28 14:01:56.277 | INFO | 0 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.278 | INFO | 1 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.279 | INFO | 3 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.280 | INFO | 7 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.280 | INFO | 5 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.283 | INFO | 8 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.284 | INFO | 4 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.285 | INFO | 2 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.286 | INFO | 6 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         * 2016-07-28 14:01:56.286 | INFO | 9 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.main | CallableFutureExample.java:52
         */
    }

}
