package com.yuzhouwan.hacker.algorithms.thread.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: ArrayBlockingQueue Example
 *
 * @author Benedict Jin
 * @since 2016/7/28
 */
public class ArrayBlockingQueueExample {

    private static final Logger _log = LoggerFactory.getLogger(ArrayBlockingQueueExample.class);

    public static void main(String[] args) {
        final MyArrayBlockingQueue myArrayBlockingQueue = new MyArrayBlockingQueue();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    _log.error("error: {}", e.getMessage());
                }
                myArrayBlockingQueue.queue1();
            }
        }).start();
        for (int i = 0; i < 10; i++) {
            myArrayBlockingQueue.queue2();
        }
    }

    private static class MyArrayBlockingQueue {

        BlockingQueue<Integer> blockingQueue1;
        BlockingQueue<Integer> blockingQueue2;

        {
            blockingQueue1 = new ArrayBlockingQueue<>(1);
            blockingQueue2 = new ArrayBlockingQueue<>(1);
            try {
                blockingQueue2.put(1);
            } catch (InterruptedException e) {
                _log.error("error: {}", e.getMessage());
            }
        }

        void queue1() {
            try {
                blockingQueue1.put(1);
                _log.info("blockingQueue1 putted  {}", Thread.currentThread().getName());
                blockingQueue2.take();
            } catch (InterruptedException e) {
                _log.error("error: {}", e.getMessage());
            }
        }

        void queue2() {
            try {
                blockingQueue2.put(1);
                _log.info("blockingQueue2 putted  {}", Thread.currentThread().getName());
                blockingQueue1.take();
            } catch (InterruptedException e) {
                _log.error("error: {}", e.getMessage());
            }
        }
        /**
         * 队列为空时，blockingQueue会阻塞
         *
         * 2016-07-28 11:07:20.472 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:20.479 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:21.106 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:21.106 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:21.128 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:21.128 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:22.109 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:22.109 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:22.419 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:22.419 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:23.241 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:23.241 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:23.392 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:23.392 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:24.284 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:24.284 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:24.918 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:24.918 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         * 2016-07-28 11:07:25.079 | INFO | blockingQueue1 putted  Thread-0 | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue1 | ArrayBlockingQueueExample.java:49
         * 2016-07-28 11:07:25.079 | INFO | blockingQueue2 putted  main | com.yuzhouwan.hacker.algorithms.thread.queue.ArrayBlockingQueueExample.queue2 | ArrayBlockingQueueExample.java:59
         */
    }

}
