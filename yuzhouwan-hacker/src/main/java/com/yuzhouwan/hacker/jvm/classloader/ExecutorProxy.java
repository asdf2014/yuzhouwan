package com.yuzhouwan.hacker.jvm.classloader;

import java.lang.reflect.Method;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Executor Proxy
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ExecutorProxy {

    private String version;
    private StandardExecutorClassLoader classLoader;

    public ExecutorProxy(String version) {
        this.version = version;
        this.classLoader = new StandardExecutorClassLoader(version);
    }

    public String execute(String name) {
        try {
            // Load ExecutorProxy class
            Class<?> executorClazz = classLoader.loadClass("com.yuzhouwan.hacker.jvm.classloader.Executor"
                    + version.toUpperCase());

            Object executorInstance = executorClazz.newInstance();
            Method method = executorClazz.getMethod("execute", String.class);

            Object obj = method.invoke(executorInstance, name);
            return obj == null ? "" : obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return name;
        }
    }
}
