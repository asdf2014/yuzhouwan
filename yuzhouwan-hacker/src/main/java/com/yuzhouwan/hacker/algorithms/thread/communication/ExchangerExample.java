package com.yuzhouwan.hacker.algorithms.thread.communication;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Exchanger Example
 *
 * @author Benedict Jin
 * @since 2016/7/27
 */
public class ExchangerExample {

    public ExchangerExample() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Exchanger<String> exchanger = new Exchanger<>();
        executorService.execute(() -> {
            try {
                String data = "yuzhouwan1";
                System.out.println(Thread.currentThread().getName() + " have " + data + " that want to exchange...");
                Thread.sleep(new Random().nextInt(4000));
                String data2 = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " get " + data2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.execute(() -> {
            try {
                String data = "yuzhouwan2";
                System.out.println(Thread.currentThread().getName() + " have " + data + " that want to exchange...");
                Thread.sleep(new Random().nextInt(4000));
                String data2 = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " get " + data2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    public static void main(String[] args) {
        new ExchangerExample();
    }

}
