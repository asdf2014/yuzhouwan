package com.yuzhouwan.hacker.effective;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Overload Tester
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class OverloadTest {

    /**
     * class file:
     * <code>
     * Object a = null;
     * System.out.println(this.a((String)null));
     * System.out.println(this.a(a));
     * </code>
     */
    @Test
    public void a() {
        Object a = null;
        assertEquals("Str null", a(null));
        assertEquals("Obj null", a(a));
    }

    private String a(String a) {
        return "Str " + a;
    }

    private Object a(Object a) {
        return "Obj " + a;
    }
}
