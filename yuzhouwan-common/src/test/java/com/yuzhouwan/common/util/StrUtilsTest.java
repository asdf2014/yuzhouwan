package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: String Stuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/23 0030
 */
public class StrUtilsTest {

    @Test
    public void fillTest() throws Exception {
        assertEquals("00000010", StrUtils.fillWitchZero(10, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.0d, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.01d, 8));
    }

    @Test
    public void cutStart() throws Exception {
        assertEquals("yuzhouwan.com", StrUtils.cutStartStr("www.yuzhouwan.com", "www."));
    }

    @Test
    public void cutMiddle() throws Exception {
        assertEquals("\\com\\yuzhouwan\\common\\util\\StrUtilsTest.class",
                StrUtils.cutMiddleStr("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\test-classes\\..\\test-classes\\com\\yuzhouwan\\common\\util\\StrUtilsTest.class",
                        "test-classes"));
    }

    @Test
    public void cutTail() throws Exception {
        assertEquals("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\",
                StrUtils.cutTailStr("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\test-classes\\",
                        "test-classes\\"));
    }
}
