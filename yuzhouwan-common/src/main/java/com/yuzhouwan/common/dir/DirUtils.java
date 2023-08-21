package com.yuzhouwan.common.dir;

import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.FileUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Directory Util
 *
 * @author Benedict Jin
 * @since 2016/4/7
 */
public final class DirUtils implements IDirUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirUtils.class);

    public static final String PROJECT_BASE_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_BASE_PATH.concat("/src/main/resources/");
    public static final String TEST_RESOURCES_PATH = PROJECT_BASE_PATH.concat("/src/test/resources/");
    private static final int BASIC_PATH_SUB_FILE_LENGTH = 6;
    private static final int BASIC_PATH_SUB_DIRECTORY_LENGTH = 6;

    /**
     * Create $PROJECT_BASE_PATH/out directory.
     *
     * @return isSuccess
     */
    public static boolean createOutDir() {
        String outDirPath = PROJECT_BASE_PATH.concat("\\out");
        File outDir = new File(outDirPath);
        LOGGER.debug(outDirPath);
        boolean isCreated = true;
        if (!outDir.exists()) isCreated = outDir.mkdir();
        if (isCreated) LOGGER.debug("OutDir:{} was created success.", outDirPath);
        else LOGGER.error(String.format("OutDir [%s] was created failed!", outDirPath));
        return isCreated;
    }

    /**
     * 获得 WEB-INF 中 lib 目录的绝对路径.
     */
    public static String getLibPathInWebApp() {
        String classesPath = getTestClassesPath();
        if (StrUtils.isEmpty(classesPath)) return null;
        return classesPath.substring(0, classesPath.lastIndexOf("/")).concat("/lib");
    }

    /**
     * 获得 target 目录的 classes 绝对路径.
     */
    public static String getClassesPath() {
        String basicPath;
        if ((basicPath = getBasicPath()) == null) {
            LOGGER.debug("Basic Path is null");
            return null;
        }
        LOGGER.debug("Basic Path: {}", basicPath);
        return basicPath.concat("/classes");
    }

    /**
     * 获得 target 目录的 test-classes 绝对路径.
     */
    public static String getTestClassesPath() {
        String basicPath = getBasicPath();
        if (basicPath == null) return null;
        return basicPath.concat("/test-classes");
    }

    /**
     * 获得 target 目录的基本 绝对路径.
     */
    public static String getBasicPath() {
        String path;
        ClassLoader classLoader;
        URL location;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            location = classLoader.getResource("/");
            if (location == null) location = classLoader.getResource("");
            if (location == null) return null;
            path = location.toURI().getPath();
            LOGGER.debug("Current Thread Location: {}", path);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        if (StrUtils.isEmpty(path)) throw new RuntimeException("Basic Path is null!");
        if (path.startsWith("file")) path = path.substring(BASIC_PATH_SUB_FILE_LENGTH);
        else if (path.startsWith("jar")) path = path.substring(BASIC_PATH_SUB_DIRECTORY_LENGTH);
        if (path.endsWith("/") || path.endsWith("\\")) path = path.substring(0, path.length() - 1);
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getProjectBasicPath() {
        String projectBasicPath = System.getProperty("user.dir");
        LOGGER.debug("Project Basic Path: {}", projectBasicPath);
        return projectBasicPath;
    }

    /**
     * 可以设置为 相对路径.
     */
    public static List<String> findPath(String path, String basePath, String fileName, boolean isAbsolute) {
        List<String> foundPath = findAbsolutePath(path, basePath, fileName);
        if (foundPath == null || isAbsolute) return foundPath;
        List<String> absolutePath = new LinkedList<>();
        for (String s : foundPath) {
            // 如果是传入空，说明是直接以 项目基础路径为开头的
            if (StrUtils.isEmpty(basePath)) absolutePath.add(StrUtils.cutStartStr(s, path));
            else absolutePath.add(StrUtils.cutMiddleStr(s, basePath));
        }
        return absolutePath;
    }

    /**
     * 遍历指定文件夹.
     */
    public static List<String> scanDir(String path) {
        if (path == null) return null;
        LOGGER.debug("Scan path: {}", path);
        List<String> wholeFiles = new LinkedList<>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> currentDirFiles = new LinkedList<>();
            dealWithSubFiles(wholeFiles, currentDirFiles, file.listFiles());
            getAllFiles(wholeFiles, currentDirFiles);
            return wholeFiles;
        } else {
            LOGGER.warn("{} is not exist!!", path);
            return null;
        }
    }

    private static void getAllFiles(List<String> wholeFiles, LinkedList<File> currentDirFiles) {
        File tempFile;
        boolean isDirectory;
        String absolutePath;
        File[] files;
        while (!currentDirFiles.isEmpty()) {
            tempFile = currentDirFiles.removeFirst();
            isDirectory = tempFile.isDirectory();
            LOGGER.debug("{} is directory: {}", tempFile.getPath(), isDirectory);
            if (!isDirectory) {
                wholeFiles.add((absolutePath = tempFile.getAbsolutePath()));
                LOGGER.debug("scanDir absolutePath is {}", absolutePath);
                continue;
            }
            if ((files = tempFile.listFiles()) == null) continue;
            dealWithSubFiles(wholeFiles, currentDirFiles, files);
        }
    }

    public static List<String> findAbsolutePath(String path, String fileName) {
        return findAbsolutePath(path, null, fileName);
    }

    /**
     * 扫描文件夹，返回指定文件名的绝对路径.
     */
    public static List<String> findAbsolutePath(String path, String basePath, String fileName) {
        if (StrUtils.isEmpty(path) || StrUtils.isEmpty(fileName)) return null;
        List<String> filePathList = scanDir(path);
        if (filePathList == null || filePathList.size() == 0) return null;
        List<String> filePathListFiltered = new LinkedList<>();
        // replaceAll DON'T support \\
        // File.separator is \ on win and / on linux
        if (StrUtils.isNotEmpty(basePath)) {
            basePath = basePath.replaceAll("/", "").concat(File.separator);
        }
        final String finalBasePath = basePath;
        filePathList.forEach(filePath -> {
            if (filePath.endsWith(fileName) && (StrUtils.isEmpty(finalBasePath) || filePath.contains(finalBasePath))) {
                filePathListFiltered.add(filePath);
            }
        });
        return filePathListFiltered;
    }

    /**
     * 对子文件进行处理，如果是子文件夹，则递归遍历下去，直到拿到所有文件的绝对路径.
     */
    private static void dealWithSubFiles(List<String> result, LinkedList<File> list, File[] files) {
        if (files == null || files.length == 0) return;
        String absolutePath;
        for (File file : files) {
            if (file.isDirectory()) list.add(file);
            result.add((absolutePath = file.getAbsolutePath()));
            LOGGER.debug(absolutePath);
        }
        LOGGER.debug("Sub Files size is {}, and Sub Directories size is {}.", result.size(), list.size());
    }

    /**
     * 返回一个目录变更的默认监控器（不间断，持续监控，只打印信息）.
     *
     * @param watchedPath the path that be watched
     */
    public static WatchRunnable buildWatchService(String watchedPath) throws Exception {
        return buildWatchService(watchedPath, null, null);
    }

    /**
     * 返回一个目录变更的监控器（不间断，持续监控，变更处理器自行指定）.
     *
     * @param watchedPath   be watched path
     * @param dealProcessor deal processor
     */
    public static WatchRunnable buildWatchService(String watchedPath, IDirUtils dealProcessor) throws Exception {
        return buildWatchService(watchedPath, dealProcessor, null);
    }

    /**
     * 返回一个目录变更的监控器.
     *
     * @param watchedPath          be watched path
     * @param waitTimeMilliseconds 监控时间间隙（ms）
     * @return WatchRunnable
     */
    public static WatchRunnable buildWatchService(String watchedPath, IDirUtils dealProcessor,
                                                  final Long waitTimeMilliseconds) throws Exception {
        if (StrUtils.isEmpty(watchedPath) || !makeSureExist(watchedPath, false)) {
            LOGGER.error(String.format("Path [%s] is a invalid path!", watchedPath));
            return null;
        }
        LOGGER.debug("Starting build watch service...");
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get(watchedPath).register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
        LOGGER.debug("Finished build watch service, and ready for watching...");
        return new WatchRunnable(watchService, dealProcessor, waitTimeMilliseconds);
    }

    /**
     * Make sure file or directory exist.
     *
     * @param path   be checked path
     * @param isFile true:  File
     *               false: Directory
     * @return isExist
     */
    public static boolean makeSureExist(final String path, final boolean isFile) {
        LOGGER.debug("Path: {}, isFile: {}", path, isFile);
        if (StrUtils.isEmpty(path)) return false;
        File file = new File(path);
        if (!FileUtils.checkExist(file)) {
            if (isFile) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    LOGGER.error("Cannot create new file!", e);
                    return false;
                }
            } else return file.mkdir();
        }
        return true;
    }

    /**
     * 处理 监控文件夹 的事件.
     */
    public void dealWithEvent(WatchEvent<?> event) {
        // could expand more processes here
        LOGGER.info("{}:\t {} event.", event.context(), event.kind());
    }
}
