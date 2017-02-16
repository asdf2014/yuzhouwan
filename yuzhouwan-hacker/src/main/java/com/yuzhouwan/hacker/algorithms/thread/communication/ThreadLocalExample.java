package com.yuzhouwan.hacker.algorithms.thread.communication;

import java.util.Random;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: ThreadLocal Example
 *
 * @author Benedict Jin
 * @since 2016/7/27
 */
public class ThreadLocalExample {

    private static ThreadLocal<Integer> x = new ThreadLocal<>();
    private static ThreadLocal<ThreadScopeThreadLocal> threadScopeThreadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                int count = 2;
                while (count > 0) {
                    count--;
                    x.set(new Random().nextInt());
                    System.out.println(Thread.currentThread().getName() + " put the data is " + x.get());

                    ThreadScopeThreadLocal myThread = ThreadScopeThreadLocal.getInstance();
                    myThread.setAge(x.get());
                    myThread.setName("yuzhouwan " + x.get());
                    threadScopeThreadLocal.set(myThread);

                    new A().getData();
                    new B().getData();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        System.out.println(String.format("Main Thread from %s get %s", Thread.currentThread().getName(), x.get()));
    }

    static class A {
        public void getData() {
            String name = threadScopeThreadLocal.get().getName();
            int data = threadScopeThreadLocal.get().getAge();
            System.out.println("A from " + Thread.currentThread().getName()
                    + ", get the name :  " + name
                    + ", get the data : " + data);
        }
    }

    static class B {
        public void getData() {
            String name = threadScopeThreadLocal.get().getName();
            int data = threadScopeThreadLocal.get().getAge();
            System.out.println("B from " + Thread.currentThread().getName()
                    + ", get the name :  " + name
                    + ", get the data : " + data);
        }
    }

}

class ThreadScopeThreadLocal {
    private static ThreadLocal<ThreadScopeThreadLocal> map = new ThreadLocal<>();
    private String name;
    private int age;

    private ThreadScopeThreadLocal() {
    }

    public static synchronized ThreadScopeThreadLocal getInstance() {
        ThreadScopeThreadLocal instance = map.get();
        if (instance == null) {
            instance = new ThreadScopeThreadLocal();
            map.set(instance);
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Thread-0 put the data is 1727979134
     * Thread-1 put the data is -472346049
     * A from Thread-0 get the name :  yuzhouwan 1727979134 get the data : 1727979134
     * A from Thread-1 get the name :  yuzhouwan -472346049 get the data : -472346049
     * B from Thread-1 get the name :  yuzhouwan -472346049 get the data : -472346049
     * B from Thread-0 get the name :  yuzhouwan 1727979134 get the data : 1727979134
     */
}
