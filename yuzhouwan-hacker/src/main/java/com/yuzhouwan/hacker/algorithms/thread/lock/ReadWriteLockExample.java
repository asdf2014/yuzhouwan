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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: ReadWriteLock Example
 *
 * @author Benedict Jin
 * @since 2016/7/27
 */
public class ReadWriteLockExample {

    private static final Logger _log = LoggerFactory.getLogger(ReadWriteLockExample.class);

    public static void main(String... args) {

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
            if (!executorService.isTerminated() || !executorService2.isTerminated()) {
                continue;
            }
            Business.getInstance().check();
            System.exit(0);
        }
    }

    public static class Business implements Serializable {

        private static final String PREFIX = "yuzhouwan|";

        private Map<Integer, String> myData = new HashMap<>();
        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        private volatile static Business instance;

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
            _log.debug("Thread: {}, count: {}, value: {}, isExist: {}", Thread.currentThread().getName(), key, value, isExist);
            return value;
        }

        /**
         * ReadWriteLock 默认非公平：
         * <p>
         * 2016-07-27 17:14:29.688 | DEBUG | Thread: pool-1-thread-1, count: 1, value: yuzhouwan|1, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * <p>
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-1, count: 1, value: yuzhouwan|1, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-7, count: 7, value: yuzhouwan|7, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-8, count: 8, value: yuzhouwan|8, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-9, count: 9, value: yuzhouwan|9, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-6, count: 6, value: yuzhouwan|6, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-4, count: 4, value: yuzhouwan|4, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-3, count: 3, value: yuzhouwan|3, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-5, count: 5, value: yuzhouwan|5, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-2, count: 2, value: yuzhouwan|2, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.737 | DEBUG | Thread: pool-2-thread-10, count: 10, value: yuzhouwan|10, isExist: false | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * <p>
         * 2016-07-27 17:14:29.743 | DEBUG | Thread: pool-1-thread-1, count: 2, value: yuzhouwan|2, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.793 | DEBUG | Thread: pool-1-thread-1, count: 3, value: yuzhouwan|3, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.844 | DEBUG | Thread: pool-1-thread-1, count: 4, value: yuzhouwan|4, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.894 | DEBUG | Thread: pool-1-thread-1, count: 5, value: yuzhouwan|5, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.944 | DEBUG | Thread: pool-1-thread-1, count: 6, value: yuzhouwan|6, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:29.994 | DEBUG | Thread: pool-1-thread-1, count: 7, value: yuzhouwan|7, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:30.044 | DEBUG | Thread: pool-1-thread-1, count: 8, value: yuzhouwan|8, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:30.094 | DEBUG | Thread: pool-1-thread-1, count: 9, value: yuzhouwan|9, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * 2016-07-27 17:14:30.144 | DEBUG | Thread: pool-1-thread-1, count: 10, value: yuzhouwan|10, isExist: true | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.isExist | ReadWriteLockExample.java:90
         * <p>
         * 2016-07-27 17:14:30.195 | INFO | Please input a number... | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.check | ReadWriteLockExample.java:95
         * 10
         * 2016-07-27 17:14:34.828 | DEBUG | Result: 10 - yuzhouwan|10 | com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample.check | ReadWriteLockExample.java:102
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
