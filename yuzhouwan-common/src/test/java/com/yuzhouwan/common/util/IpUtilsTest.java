package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Ip Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/7 0030
 */
public class IpUtilsTest {

    @Test
    public void checkValidTest() throws Exception {
        assertEquals(true, IpUtils.checkValid("1.1.1.1"));
        assertEquals(true, IpUtils.checkValid("113.12.83.4"));
        assertEquals(false, IpUtils.checkValid("313.12.83.4"));
    }

    @Test
    public void removeTail32Test() throws Exception {
        assertEquals("1.1.1.1", IpUtils.removeTail32("1.1.1.1/32"));
        assertEquals("113.12.83.4", IpUtils.removeTail32("113.12.83.4/32"));
    }

    @Test
    public void extractDomainTest() throws Exception {
        assertEquals("yuzhouwan.com", IpUtils.extractDomain("http://yuzhouwan.com"));
        assertEquals("yuzhouwan.com", IpUtils.extractDomain("http://yuzhouwan.com/subpath/welcome.html"));
        assertEquals(null, IpUtils.extractDomain("http:/yuzhouwan.com"));

        assertEquals(null, IpUtils.extractDomain(""));
        assertEquals(null, IpUtils.extractDomain(null));
    }
}
