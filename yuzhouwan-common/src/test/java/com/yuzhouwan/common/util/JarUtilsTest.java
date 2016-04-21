package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Benedict Jin on 2016/4/20.
 */
public class JarUtilsTest {
    @Test
    public void listDirWithinJarTest() throws Exception {
        assertEquals("yuzhouwan.com", JarUtils.getProperty("site.domain"));
        assertEquals("asdf's blog", JarUtils.getProperty("blog.name"));

        assertEquals("2.6.2", JarUtils.getProperty("hadoop.version"));
    }
}
