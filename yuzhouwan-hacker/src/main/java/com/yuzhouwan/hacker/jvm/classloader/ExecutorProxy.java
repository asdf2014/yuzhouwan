package com.yuzhouwan.hacker.jvm.classloader;

import java.lang.reflect.Method;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Executor Proxy
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ExecutorProxy implements Executor {

    private final String version;
    private final StandardExecutorClassLoader classLoader;

    ExecutorProxy(String version) {
        this.version = version;
        this.classLoader = new StandardExecutorClassLoader(version);
    }

    @Override
    public String execute(String name) {
        try {
            // Load ExecutorProxy class
            Class<?> executorClass = classLoader.loadClass(
                    "com.yuzhouwan.hacker.jvm.classloader.Executor".concat(version.toUpperCase()));
            Object executorInstance = executorClass.newInstance();
            Method method = executorClass.getMethod("execute", String.class);
            Object obj = method.invoke(executorInstance, name);
            return obj == null ? "" : obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return name;
        }
    }
}
