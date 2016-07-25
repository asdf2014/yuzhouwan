package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.LinkedList;

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
    public void mainValueTest() throws Exception {
        if (1 == Integer.parseInt(StrUtils.getMainValue("ATK000001", "ATK".length(), "0"))) {
            System.out.println("1");
        }
        if (40 == Integer.parseInt(StrUtils.getMainValue("ATK000040", "ATK".length(), "0"))) {
            System.out.println("40");
        }
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

    @Test
    public void holderTest() throws Exception {
        assertEquals("a1b2c3", String.format("%s1b%Sc%d", "a", "2", 3));
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        assertEquals("a b", String.format("%s %s", linkedList.toArray()));
    }
}
