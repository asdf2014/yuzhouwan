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

    @Override
    public String execute(final String name) {
        return this.handle(new Handler() {
            @Override
            public String handle() {
                return "V:" + name;
            }
        });
    }

    String handle(Handler handler) {
        return handler.call();
    }
}
