package com.yuzhouwan.hacker.algorithms.thread.sync;

/**
 * Copyright @ 2017 suning.com
 * All right reserved.
 * Function：Sync Example
 *
 * @author Benedict Jin
 * @since 2017/6/26
 */
public class SyncExample {

    /*
    Thread: Thread-0, Result: 0
    Begin sleeping...
    Thread: main, Result: 0                 # 说明 synchronized标记的方法，不会对"传参"加锁
    Thread: main, start sync method
    End sleeping...
    Thread: Thread-0, Result: 0
    Thread: main, Result: 0
    Begin sleeping...
    End sleeping...
    Thread: main, Result: 0
    Thread: main, end sync method           # 说明 synchronized标记的方法，只会对"方法的调用"加锁
     */
    public static void main(String[] args) throws InterruptedException {
        SyncObj syncObj = new SyncObj();
        new Thread(() -> {
            try {
                sync(syncObj);
            } catch (InterruptedException ignored) {
            }
        }).start();
        Thread.sleep(1000);
        int num = syncObj.getNum();
        debug(num);
        System.out.println(String.format("Thread: %s, start sync method", Thread.currentThread().getName()));
        sync(syncObj);
        System.out.println(String.format("Thread: %s, end sync method", Thread.currentThread().getName()));
    }

    private static synchronized void sync(SyncObj syncObj) throws InterruptedException {
        int num = syncObj.getNum();
        debug(num);
        System.out.println("Begin sleeping...");
        Thread.sleep(3000);
        System.out.println("End sleeping...");
        num = syncObj.getNum();
        debug(num);
    }

    private static void debug(int num) {
        System.out.println(String.format("Thread: %s, Result: %d", Thread.currentThread().getName(), num));
    }
}

class SyncObj {
    public int getNum() {
        return 0;
    }
}
