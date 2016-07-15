package com.yuzhouwan.common.util;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Directory Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/7 0030
 */
public class DirUtilsTest {

    public static void main(String[] args) {
        DirUtils.createOutDir();
    }

    @Test
    public void testClassPath() {
        /*
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target
         */
        System.out.println(DirUtils.getBasicPath());
        /*
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/classes
         */
        System.out.println(DirUtils.getClassesPath());
        /*
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/test-classes
         */
        System.out.println(DirUtils.getTestClassesPath());
    }

    @Test
    public void testLibPath() {
        /*
         * /F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-common/target/lib
         */
        System.out.println(DirUtils.getLibPathInWebApp());
    }

    @Test
    public void testDirScan() throws Exception {
        List<String> absolutePath;
        assertNotEquals(null, absolutePath = DirUtils.findAbsolutePath(DirUtils.getTestClassesPath(), "DirUtilsTest.class"));
        assertEquals(true, absolutePath.get(0).endsWith("test-classes\\com\\yuzhouwan\\common\\util\\DirUtilsTest.class"));
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
        DirUtils.findPath(DirUtils.getClassesPath(), ".properties", true, "prop").forEach(System.out::println);
    }

    @Test
    public void testBuildWatchService() throws Exception {
        DirUtils.WatchRunnable thread = DirUtils.buildWatchService("Z:/watch");
        assertEquals(null, thread);

        // 2 ms is lowest limitation for me ;)
        thread = DirUtils.buildWatchService("E:/watch", null, 2L);
        assertNotEquals(null, thread);
        Thread t = new Thread(thread);
        t.start();
        DirUtils.makeSureExist("E:/watch/a", false);
        DirUtils.makeSureExist("E:/watch/b/", false);
        DirUtils.makeSureExist("E:/watch/c.txt", true);

        new File("E:/watch/a").delete();
        new File("E:/watch/b/").delete();
        new File("E:/watch/c.txt").delete();

        new File("E:/watch").deleteOnExit();

        thread.setRunning(false);
        t.join();
    }

}