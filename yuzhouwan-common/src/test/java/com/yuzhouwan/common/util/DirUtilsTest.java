package com.yuzhouwan.common.util;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.common.dir.WatchRunnable;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Directory Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/7
 */
public class DirUtilsTest {

    public static void main(String[] args) {
        System.out.println(DirUtils.createOutDir());
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
    public void testDirScan() {
        List<String> absolutePath;
        assertNotEquals(null, absolutePath = DirUtils.findAbsolutePath(DirUtils.getTestClassesPath(),
                "DirUtilsTest.class"));
        assertTrue(absolutePath.get(0).endsWith("test-classes" + File.separator + "com" + File.separator +
                "yuzhouwan" + File.separator + "common" + File.separator + "util" + File.separator +
                "DirUtilsTest.class"));
    }

    @Test
    public void testDirScanAbsolute() {
        assertEquals(File.separator + "com" + File.separator + "yuzhouwan" + File.separator + "common" +
                File.separator + "util" + File.separator + "DirUtilsTest.class", DirUtils.findPath(
                DirUtils.getTestClassesPath(), "test-classes", "DirUtilsTest.class", false)
                .get(0));
    }

    @Test
    public void testJarPath() {
        assertEquals(new LinkedList<>(Collections.singletonList(File.separator + "yuzhouwan-common.jar")),
                DirUtils.findPath(DirUtils.getTestClassesPath(), "lib", ".jar", false));
    }

    @Test
    public void testPropertiesPath() {
        DirUtils.findPath(DirUtils.getClassesPath(), "prop", ".properties", true).forEach(System.out::println);
    }

    @Ignore
    @Test
    public void testBuildWatchService() throws Exception {
        WatchRunnable thread = DirUtils.buildWatchService("Z:/watch");
        assertNull(thread);

        // 2 ms is lowest limitation for me ;)
        thread = DirUtils.buildWatchService("E:/watch", null, 2L);
        assertNotEquals(null, thread);
        Thread t = new Thread(thread);
        t.start();
        DirUtils.makeSureExist("E:/watch/a", false);
        DirUtils.makeSureExist("E:/watch/b/", false);
        DirUtils.makeSureExist("E:/watch/c.txt", true);

        assertTrue(new File("E:/watch/a").delete());
        assertTrue(new File("E:/watch/b/").delete());
        assertTrue(new File("E:/watch/c.txt").delete());

        new File("E:/watch").deleteOnExit();

        if (thread != null) thread.setRunning(false);
        t.join();
    }
}
