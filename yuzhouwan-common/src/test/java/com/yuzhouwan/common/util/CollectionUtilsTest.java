package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Collection Util Tester
 *
 * @author Benedict Jin
 * @since 2016/6/12 0030
 */
public class CollectionUtilsTest {

    @Test
    public void removeAllByStrWithSeparator() throws Exception {

        assertEquals(Arrays.asList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a,c", ","));
        assertEquals(Arrays.asList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a", ","));
        assertEquals(Arrays.asList("aaa", "bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "c", ","));
        assertEquals(Arrays.asList(),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "", ","));

        assertEquals(Arrays.asList(""),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList(""), "a", ","));
    }

    @Test
    public void duplicate() throws Exception {

        Integer[] a = new Integer[]{1, 2, 3};
        Integer[] b = new Integer[]{3};
        Integer[] c = new Integer[]{4};
        Integer[] d = null;

        assertEquals(3, CollectionUtils.duplicate(a, b)[0]);
        assertEquals(0, CollectionUtils.duplicate(b, c).length);
        assertEquals(true, CollectionUtils.duplicate(c, d) == null);
    }
}
