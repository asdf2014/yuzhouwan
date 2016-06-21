package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Directory Util
 *
 * @author Benedict Jin
 * @since 2016/4/7 0030
 */
public class DirUtils {

    private static final Logger _log = LoggerFactory.getLogger(DirUtils.class);
    public static final String RESOURCES_PATH = System.getProperty("user.dir").concat("/src/main/resources/");
    public static final String TEST_RESOURCES_PATH = System.getProperty("user.dir").concat("/src/test/resources/");

    public static void createOutDir() {

        String currentDir = System.getProperty("user.dir");
        String outDirPath = currentDir.concat("\\out");
        File outDir = new File(outDirPath);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        _log.debug(currentDir);
    }

    /**
     * 获得 WEB-INF 中 lib 目录的绝对路径
     *
     * @return
     */
    public static String getLibPathInWebApp() {
        String classesPath = getTestClassesPath();
        return classesPath.substring(0, classesPath.lastIndexOf("/")).concat("/lib");
    }

    /**
     * 获得 target 目录的 test-classes绝对路径
     *
     * @return
     */
    public static String getClassesPath() {
        return getBasicPath().concat("/classes");
    }


    /**
     * 获得 target 目录的 test-classes绝对路径
     *
     * @return
     */
    public static String getTestClassesPath() {
        return getBasicPath().concat("/test-classes");
    }

    /**
     * 获得 target 目录的基本 绝对路径
     *
     * @return
     */
    public static String getBasicPath() {
        String path = "";
        try {
            path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path.startsWith("file")) {
            path = path.substring(6);
        } else if (path.startsWith("jar")) {
            path = path.substring(10);
        }
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length() - 1);
        }
        return path.substring(0, path.lastIndexOf("/"));
    }

    /**
     * 可以设置为 相对路径
     *
     * @param path
     * @param fileName
     * @param isAbsolute
     * @return
     */
    public static List<String> findPath(String path, String fileName, boolean isAbsolute, String basePath) {
        List<String> foundPath = findAbsolutePath(path, fileName);
        if (foundPath == null || isAbsolute) {
            return foundPath;
        }
        List<String> absolutePath = new LinkedList<>();
        for (String s : foundPath) {
            absolutePath.add(StrUtils.cutMiddleStr(s, basePath));
        }
        return absolutePath;
    }

    /**
     * 扫描文件夹，返回指定文件名的绝对路径
     *
     * @param path
     * @param fileName
     * @return
     */
    public static List<String> findAbsolutePath(String path, String fileName) {
        if (StrUtils.isEmpty(path) || StrUtils.isEmpty(fileName))
            return null;
        List<String> filePathList = scanDir(path);
        if (filePathList == null || filePathList.size() == 0) {
            return null;
        }
        List<String> filePathListFiltered = new LinkedList<>();
        for (String filePath : filePathList) {
            if (filePath.endsWith(fileName))
                filePathListFiltered.add(filePath);
        }
        return filePathListFiltered;
    }

    /**
     * 遍历指定文件夹
     *
     * @param path
     * @return
     */
    public static List<String> scanDir(String path) {
        if (path == null)
            return null;
        List<String> result = new LinkedList<>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<>();
            File[] files = file.listFiles();
            dealWithSubFiles(result, list, files);
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                if (files == null)
                    continue;
                dealWithSubFiles(result, list, files);
            }
            return result;
        } else {
            _log.error("{} is not exist!!", path);
            return null;
        }
    }

    /**
     * 对子文件进行处理
     *
     * @param result
     * @param list
     * @param files
     */
    private static void dealWithSubFiles(List<String> result, LinkedList<File> list, File[] files) {
        for (File file2 : files) {
            if (file2.isDirectory()) {
                list.add(file2);
            }
            result.add(file2.getAbsolutePath());
            _log.debug(file2.getAbsolutePath());
        }
    }
}
