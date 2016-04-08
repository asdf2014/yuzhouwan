package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Properties Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/8 0030
 */
public class PropUtilsTest {

    @Test
    public void getPropTester() throws Exception {

        String confPath = System.getProperty("user.dir").concat("\\src\\main\\resources\\conf.txt");
        PropUtils p = new PropUtils(confPath);
        assertEquals("asdf's blog", p.getProperty("blog.name"));
        assertEquals("yuzhouwan.com", p.getProperty("site.domain"));
    }
}
