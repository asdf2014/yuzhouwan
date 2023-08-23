package com.yuzhouwan.hacker.annotation;

import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：GuardedBy Example
 *
 * @author Benedict Jin
 * @since 2019-01-22
 */
public class GuardedByExample {

    @GuardedBy("obj")
    private final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
    private final Object obj = new Object();

    public void put(String k, String v) {
        synchronized (obj) {
            map.put(k, v);
        }
    }

    /**
     * If you use `error prone` tool to check this, this annotation should be `@SuppressWarnings("GuardedBy")`
     * {@see https://errorprone.info/bugpattern/GuardedBy}
     * {@see https://github.com/apache/incubator-druid/pull/6868#discussion_r249639199}
     */
    @SuppressWarnings("FieldAccessNotGuarded")
    public void remove(String k) {
        map.remove(k);
    }

    @Override
    public String toString() {
        synchronized (obj) {
            return "GuardedByExample{" +
                    "map=" + map +
                    '}';
        }
    }
}
