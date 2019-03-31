package com.yuzhouwan.hacker.jvm.classloader;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Handler
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
abstract class Handler {

    public abstract String handle();

    public String call() {
        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(AbstractExecutor.class.getClassLoader());
        String name = handle();
        currentThread.setContextClassLoader(oldClassLoader);
        return name;
    }
}
