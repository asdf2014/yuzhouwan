package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Benedict Jin on 2016/4/7.
 */
public class DirUtilsTest {

    public static void main(String[] args) {
        DirUtils.createOutDir();
    }

    @Test
    public void testClassPath() {
        /**
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target
         */
        System.out.println(DirUtils.getBasicPath());
        /**
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/classes
         */
        System.out.println(DirUtils.getClassesPath());
        /**
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/test-classes
         */
        System.out.println(DirUtils.getTestClassesPath());
    }

    @Test
    public void testLibPath() {
        /**
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/lib
         */
        System.out.println(DirUtils.getLibPathInWebApp());
    }

    @Test
    public void testDirScan() throws Exception {
        assertEquals(true, DirUtils.findAbsolutePath(DirUtils.getTestClassesPath(), "DirUtilsTest.class").get(0).endsWith(
                "test-classes\\com\\yuzhouwan\\common\\util\\DirUtilsTest.class"));
    }

    @Test
    public void testDirScanAbsolute() throws Exception {
        assertEquals("\\com\\yuzhouwan\\common\\util\\DirUtilsTest.class", DirUtils.findPath(
                DirUtils.getTestClassesPath(), "DirUtilsTest.class", false, "test-classes")
                .get(0));
    }

    @Test
    public void testJarPath() throws Exception {
        assertEquals(new LinkedList<>(Arrays.asList("\\yuzhouwan-common.jar")),
                DirUtils.findPath(DirUtils.getTestClassesPath(), ".jar", false, "lib"));
    }

    @Test
    public void testPropertiesPath() throws Exception {
        DirUtils.findPath(DirUtils.getClassesPath(), ".properties", true, "prop").forEach(propFile ->
                System.out.println(propFile));
    }

}