package com.yuzhouwan.hacker.jvm.classloader;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Executor V2
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ExecutorV2 extends AbstractExecutor {

    @Override
    public String execute(final String name) {
        return this.handle(new Handler() {
            @Override
            public String handle() {
                return "V2:".concat(name);
            }
        });
    }
}
