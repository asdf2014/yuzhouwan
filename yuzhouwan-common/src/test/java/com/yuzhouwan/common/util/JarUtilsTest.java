package com.yuzhouwan.common.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Jar Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/20
 */
public class JarUtilsTest {

    @Ignore
    @Test
    public void listDirWithinJarTest() {
        assertEquals("yuzhouwan.com", JarUtils.getInstance().getProperty("site.domain"));
        assertEquals("asdf's blog", JarUtils.getInstance().getProperty("blog.name"));

        assertEquals("2.6.2", JarUtils.getInstance().getProperty("hadoop.version"));
    }

    @Test
    public void locationTest() {
        assertTrue(JarUtils.isProjectJar(JarUtilsTest.class));
    }
}
