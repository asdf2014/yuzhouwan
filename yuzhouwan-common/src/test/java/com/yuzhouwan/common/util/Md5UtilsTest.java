package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Md5 Utils Test
 *
 * @author Benedict Jin
 * @since 2017/12/7
 */
public class Md5UtilsTest {

    @Test
    public void testMd5() {
        assertEquals("24878de96952d64e8149af10f1e96066", Md5Utils.md5("yuzhouwan.com"));
    }
}
