package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Jar Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/20 0030
 */
public class JarUtilsTest {

    @Test
    public void listDirWithinJarTest() throws Exception {
        assertEquals("yuzhouwan.com", JarUtils.getProperty("site.domain"));
        assertEquals("asdf's blog", JarUtils.getProperty("blog.name"));

        assertEquals("2.6.2", JarUtils.getProperty("hadoop.version"));
    }

    @Test
    public void locationTest() throws Exception {
        assertEquals(true, JarUtils.isProjectJar(JarUtilsTest.class));
    }
}
