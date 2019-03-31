package com.yuzhouwan.hacker.annotation;

import org.junit.Test;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：GuardedBy Test
 *
 * @author Benedict Jin
 * @since 2019-01-22
 */
public class GuardedByTest {

    @Test
    public void test() {
        GuardedByExample guarded = new GuardedByExample();
        guarded.put("a", "a");
        guarded.put("b", "b");
        guarded.put("c", "c");

        guarded.remove("b");
    }
}
