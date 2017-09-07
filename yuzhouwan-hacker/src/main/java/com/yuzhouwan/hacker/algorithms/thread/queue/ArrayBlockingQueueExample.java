package com.yuzhouwan.hacker.algorithms.thread.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Copyright @ 2017 yuzhouwan.com
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

    /**
     * MyArrayBlockingQueue.
     */
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
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         * blockingQueue1 putted  Thread-0
         * blockingQueue2 putted  main
         */
    }
}
