package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Properties Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/8
 */
public class PropUtilsTest {

    private static final PropUtils p = PropUtils.getInstance();

    @Test
    public void getPropTest() {
        assertEquals("asdf's blog", p.getProperty("blog.name"));
        assertEquals("yuzhouwan.com", p.getProperty("site.domain"));
    }

    @Test
    public void getPropWithDefaultValueTest() {
        {
            assertEquals("asdf's blog", p.getProperty("blog.name", "asdf's blog"));
            assertEquals("yuzhouwan.com", p.getProperty("site.domain", ""));
        }
        {
            assertEquals("Benedict Jin", p.getProperty("blog.short.name", "Benedict Jin"));
            assertEquals("", p.getProperty("site.domain2", ""));
        }
    }
}
