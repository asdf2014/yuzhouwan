package com.yuzhouwan.hacker.algorithms.thread.pool;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：WorkerThread
 *
 * @author Benedict Jin
 * @since 2015/12/29
 */
class WorkerThread implements Runnable {

    private String command;

    WorkerThread(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End.");
    }

    private void processCommand() {
        try {
            int time = (int) (Math.random() * 6000);
            System.out.println("Time: " + time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.command;
    }
}