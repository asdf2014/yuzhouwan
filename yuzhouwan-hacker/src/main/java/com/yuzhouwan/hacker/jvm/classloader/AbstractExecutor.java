package com.yuzhouwan.hacker.jvm.classloader;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Abstract Executor
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class AbstractExecutor implements Executor {

    /**
     * Handler.
     */
    protected abstract class Handler {

        public String call() {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

            // 临时更改 ClassLoader.
            Thread.currentThread().setContextClassLoader(AbstractExecutor.class.getClassLoader());

            String name = handle();

            // 还原为之前的 ClassLoader.
            Thread.currentThread().setContextClassLoader(oldClassLoader);

            return name;
        }

        public abstract String handle();
    }

    protected String handle(Handler handler) {
        return handler.call();
    }

    @Override
    public String execute(final String name) {
        return this.handle(new Handler() {
            @Override
            public String handle() {
                return "V:" + name;
            }
        });
    }
}
