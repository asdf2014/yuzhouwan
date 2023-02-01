package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Digest Utils Test
 *
 * @author Benedict Jin
 * @since 2017/12/7
 */
public class DigestUtilsTest {

    @Test
    public void testDigest() {
        assertEquals("1f3f67eb86d0931ce91e710f3dd26677a18799823df12ebfa4beb8d4d7da695", DigestUtils.digest("yuzhouwan.com"));
    }
}
