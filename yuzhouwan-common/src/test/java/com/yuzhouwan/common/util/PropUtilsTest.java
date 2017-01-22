package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Properties Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/8 0030
 */
public class PropUtilsTest {

    @Test
    public void getPropTester() throws Exception {
        assertEquals("asdf's blog", PropUtils.getInstance().getProperty("blog.name"));
        assertEquals("yuzhouwan.com", PropUtils.getInstance().getProperty("site.domain"));
    }
}
