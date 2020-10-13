package com.yuzhouwan.hacker.algorithms.thread.semaphore;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Semaphore Example
 *
 * @author Benedict Jin
 * @since 2020/10/14
 */
public class SemaphoreExample implements Runnable {

    private static final Semaphore S = new Semaphore(5);
    private static final List<Long> THREAD_ID_LIST = new LinkedList<>();
    private static final Set<Long> THREAD_ID_SET = new HashSet<>();

    @Override
    public void run() {
        try {
            S.acquire();  // acquire the semaphore
            business();  // actually business logic
            record();  // record the timestamp when it was completed
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            S.release();  // release the semaphore
        }
    }

    private void record() {
        long id = Thread.currentThread().getId();
        THREAD_ID_LIST.add(id);
        THREAD_ID_SET.add(id);
        System.out.println(new Date().getTime() + " : " + id);
    }

    private void business() throws InterruptedException {
        Thread.sleep(100);
    }

    /*
     1602659425001 : 12
     1602659425001 : 11
     1602659425001 : 13
     1602659425001 : 14
     1602659425001 : 15
     1602659425102 : 18
     1602659425102 : 16
     1602659425102 : 19
     1602659425102 : 17
     1602659425102 : 20
     1602659425203 : 11
     1602659425203 : 13
     1602659425203 : 14
     1602659425203 : 12
     1602659425203 : 16
     1602659425304 : 15
     1602659425304 : 17
     1602659425304 : 19
     1602659425304 : 20
     1602659425304 : 18
     set size : 12
     list size : 17
     */
    public static void main(String[] args) throws InterruptedException {
        final ExecutorService exec = Executors.newFixedThreadPool(10);
        final SemaphoreExample demo = new SemaphoreExample();
        for (int i = 0; i < 20; ++i) {
            exec.submit(demo);
        }
        exec.shutdown();
        for (; ; ) {
            if (exec.isTerminated()) {
                Thread.sleep(10);
                break;
            }
        }
        System.out.println("set size : " + THREAD_ID_SET.size());
        System.out.println("list size : " + THREAD_ID_LIST.size());
    }
}
