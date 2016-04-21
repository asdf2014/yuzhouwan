package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Benedict Jin on 2016/4/7.
 */
public class DirUtilsTest {

    public static void main(String[] args) {
        DirUtils.createOutDir();
    }

    @Test
    public void testDirScan() throws Exception {
        assertEquals(true, DirUtils.findPath(DirUtils.getClassesPath(), "DirUtilsTest.class").get(0).endsWith(
                "test-classes\\com\\yuzhouwan\\common\\util\\DirUtilsTest.class"));
    }

    @Test
    public void testDirScanAbsolute() throws Exception {
        assertEquals("\\com\\yuzhouwan\\common\\util\\DirUtilsTest.class", DirUtils.findPath(
                DirUtils.getClassesPath(), "DirUtilsTest.class", false, "test-classes")
                .get(0));
    }

}