package com.yuzhouwan.hacker.algorithms.thread.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: ReadWriteLock Example
 *
 * @author Benedict Jin
 * @since 2016/7/27
 */
public class ReadWriteLockExample {

    private static final Logger _log = LoggerFactory.getLogger(ReadWriteLockExample.class);

    public static void main(String... args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            int count = 0;
            while (true) {
                Business.getInstance().isExist(++count);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    _log.error("error: {}", e.getMessage());
                }
                if (count == 10) return;
            }
        });
        executorService.shutdown();

        ExecutorService executorService2 = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService2.execute(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    _log.error("error: {}", e.getMessage());
                }
                Business.getInstance().isExist(task + 1);
            });
        }
        executorService2.shutdown();

        for (; ; ) {
            if (!executorService.awaitTermination(1000L, TimeUnit.MILLISECONDS)
                    || !executorService2.awaitTermination(1000L, TimeUnit.MILLISECONDS)) continue;
            Business.getInstance().check();
            System.exit(0);
        }
    }

    /**
     * Business.
     */
    public static final class Business implements Serializable {

        private static final String PREFIX = "yuzhouwan|";
        private static volatile Business instance;
        private Map<Integer, String> myData = new HashMap<>();
        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        private Business() {
        }

        public static Business getInstance() {
            if (instance == null)
                synchronized (Business.class) {
                    if (instance == null)
                        instance = new Business();
                }
            return instance;
        }

        /**
         * Check Data that is existed in HashMap.
         */
        private String isExist(int key) {
            boolean isExist;
            Lock readLock = readWriteLock.readLock();
            try {
                readLock.lock();
                isExist = myData.containsKey(key);
            } finally {
                readLock.unlock();
            }
            String value = PREFIX + key;
            if (!isExist) {
                Lock writeLock = readWriteLock.writeLock();
                try {
                    writeLock.lock();
                    myData.put(key, value);
                } finally {
                    writeLock.unlock();
                }
            }
            _log.debug("Thread: {}, count: {}, value: {}, isExist: {}",
                    Thread.currentThread().getName(), key, value, isExist);
            return value;
        }

        /**
         * ReadWriteLock 默认非公平：
         * <p>
         * Thread: pool-1-thread-1, count: 1, value: yuzhouwan|1, isExist: false
         * <p>
         * Thread: pool-2-thread-1, count: 1, value: yuzhouwan|1, isExist: true
         * Thread: pool-2-thread-7, count: 7, value: yuzhouwan|7, isExist: false
         * Thread: pool-2-thread-8, count: 8, value: yuzhouwan|8, isExist: false
         * Thread: pool-2-thread-9, count: 9, value: yuzhouwan|9, isExist: false
         * Thread: pool-2-thread-6, count: 6, value: yuzhouwan|6, isExist: false
         * Thread: pool-2-thread-4, count: 4, value: yuzhouwan|4, isExist: false
         * Thread: pool-2-thread-3, count: 3, value: yuzhouwan|3, isExist: false
         * Thread: pool-2-thread-5, count: 5, value: yuzhouwan|5, isExist: false
         * Thread: pool-2-thread-2, count: 2, value: yuzhouwan|2, isExist: false
         * Thread: pool-2-thread-10, count: 10, value: yuzhouwan|10, isExist: false
         * <p>
         * Thread: pool-1-thread-1, count: 2, value: yuzhouwan|2, isExist: true
         * Thread: pool-1-thread-1, count: 3, value: yuzhouwan|3, isExist: true
         * Thread: pool-1-thread-1, count: 4, value: yuzhouwan|4, isExist: true
         * Thread: pool-1-thread-1, count: 5, value: yuzhouwan|5, isExist: true
         * Thread: pool-1-thread-1, count: 6, value: yuzhouwan|6, isExist: true
         * Thread: pool-1-thread-1, count: 7, value: yuzhouwan|7, isExist: true
         * Thread: pool-1-thread-1, count: 8, value: yuzhouwan|8, isExist: true
         * Thread: pool-1-thread-1, count: 9, value: yuzhouwan|9, isExist: true
         * Thread: pool-1-thread-1, count: 10, value: yuzhouwan|10, isExist: true
         * <p>
         * Please input a number...
         * 10
         * Result: 10 - yuzhouwan|10
         * <p>
         * Process finished with exit code 0
         */
        void check() {
            _log.info("Please input a number...");
            int input = 1;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                input = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                _log.error("error: {}", e.getMessage());
            }
            _log.debug("Result: {} - {}", input, myData.get(input));
        }
    }
}
