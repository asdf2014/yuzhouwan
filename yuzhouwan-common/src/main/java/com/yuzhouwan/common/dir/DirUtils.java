package com.yuzhouwan.common.dir;

import com.yuzhouwan.common.util.ExceptionUtils;
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
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Directory Util
 *
 * @author Benedict Jin
 * @since 2016/4/7 0030
 */
public class DirUtils implements IDirUtils {

    private static final Logger _log = LoggerFactory.getLogger(DirUtils.class);

    public static final String PROJECT_BASE_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_BASE_PATH.concat("/src/main/resources/");
    public static final String TEST_RESOURCES_PATH = PROJECT_BASE_PATH.concat("/src/test/resources/");

    /**
     * Create $PROJECT_BASE_PATH/out directory.
     *
     * @return isSuccess
     */
    public static boolean createOutDir() {
        String outDirPath = PROJECT_BASE_PATH.concat("\\out");
        File outDir = new File(outDirPath);
        _log.debug(outDirPath);
        boolean isCreated = true;
        if (!outDir.exists()) isCreated = outDir.mkdir();
        if (isCreated)
            _log.debug("OutDir:{} was created success.", outDirPath);
        else
            _log.error("OutDir:{} was created failed!", outDirPath);
        return isCreated;
    }

    /**
     * 获得 WEB-INF 中 lib 目录的绝对路径
     *
     * @return
     */
    public static String getLibPathInWebApp() {
        String classesPath = getTestClassesPath();
        if (StrUtils.isEmpty(classesPath)) return null;
        return classesPath.substring(0, classesPath.lastIndexOf("/")).concat("/lib");
    }

    /**
     * 获得 target 目录的 classes绝对路径
     *
     * @return
     */
    public static String getClassesPath() {
        String basicPath;
        if ((basicPath = getBasicPath()) == null) {
            _log.debug("Basic Path is null");
            return null;
        }
        _log.debug("Basic Path: {}", basicPath);
        return basicPath.concat("/classes");
    }


    /**
     * 获得 target 目录的 test-classes绝对路径
     *
     * @return
     */
    public static String getTestClassesPath() {
        String basicPath;
        if ((basicPath = getBasicPath()) == null) {
            return null;
        }
        return basicPath.concat("/test-classes");
    }

    /**
     * 获得 target 目录的基本 绝对路径
     *
     * @return
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
            _log.debug("Current Thread Location: {}", path);
        } catch (Exception e) {
            _log.error("{}", ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        if (StrUtils.isEmpty(path)) throw new RuntimeException("Basic Path is null!!!");
        if (path.startsWith("file")) {
            path = path.substring(6);
        } else if (path.startsWith("jar")) {
            path = path.substring(10);
        }
        if (path.endsWith("/") || path.endsWith("\\")) path = path.substring(0, path.length() - 1);
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getProjectBasicPath() {
        String projectBasicPath = System.getProperty("user.dir");
        _log.debug("Project Basic Path: {}", projectBasicPath);
        return projectBasicPath;
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
        List<String> foundPath = findAbsolutePath(path, fileName, basePath);
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
     * 遍历指定文件夹
     *
     * @param path
     * @return
     */
    public static List<String> scanDir(String path) {
        if (path == null) return null;
        _log.debug("Scan path: {}", path);
        List<String> result = new LinkedList<>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<>();
            File[] files = file.listFiles();
            dealWithSubFiles(result, list, files);
            File tempFile;
            boolean isDirectory;
            String absolutePath;
            while (!list.isEmpty()) {
                tempFile = list.removeFirst();
                _log.debug("{} isDirectory: {}", tempFile.getPath(), (isDirectory = tempFile.isDirectory()));
                if (!isDirectory) {
                    result.add((absolutePath = tempFile.getAbsolutePath()));
                    _log.debug("scanDir absolutePath is {}", absolutePath);
                    continue;
                }
                if ((files = tempFile.listFiles()) == null) continue;
                dealWithSubFiles(result, list, files);
            }
            return result;
        } else {
            _log.error("{} is not exist!!", path);
            return null;
        }
    }

    public static List<String> findAbsolutePath(String path, String fileName) {
        return findAbsolutePath(path, fileName, null);
    }

    /**
     * 扫描文件夹，返回指定文件名的绝对路径
     *
     * @param path
     * @param fileName
     * @return
     */
    public static List<String> findAbsolutePath(String path, String fileName, String basePath) {
        if (StrUtils.isEmpty(path) || StrUtils.isEmpty(fileName)) return null;
        List<String> filePathList = scanDir(path);
        if (filePathList == null || filePathList.size() == 0) return null;
        List<String> filePathListFiltered = new LinkedList<>();
        // replaceAll DON'T support \\
        // File.separator is \ on win and / on linux
        if (StrUtils.isNotEmpty(basePath)) basePath = basePath.replaceAll("/", "").concat(File.separator);
        final String finalBasePath = basePath;
        filePathList.forEach(filePath -> {
            if (filePath.endsWith(fileName) && (StrUtils.isEmpty(finalBasePath) || filePath.contains(finalBasePath)))
                filePathListFiltered.add(filePath);
        });
        return filePathListFiltered;
    }

    /**
     * 对子文件进行处理，如果是子文件夹，则递归遍历下去，直到拿到所有文件的绝对路径
     *
     * @param result
     * @param list
     * @param files
     */
    private static void dealWithSubFiles(List<String> result, LinkedList<File> list, File[] files) {
        if (files == null || files.length == 0) return;
        String absolutePath;
        for (File file : files) {
            if (file.isDirectory()) list.add(file);
            result.add((absolutePath = file.getAbsolutePath()));
            _log.debug(absolutePath);
        }
        _log.debug("Sub Files size is {}, and Sub Directories size is {}", result.size(), list.size());
    }

    /**
     * 返回一个目录变更的默认监控器 (不间断，持续监控，只打印信息)
     *
     * @param watchedPath be watched path
     * @return
     * @throws Exception
     */
    public static WatchRunnable buildWatchService(String watchedPath) throws Exception {
        return buildWatchService(watchedPath, null, null);
    }

    /**
     * 返回一个目录变更的监控器 (不间断，持续监控，变更处理器自行指定)
     *
     * @param watchedPath   be watched path
     * @param dealProcessor deal processor
     * @return
     * @throws Exception
     */
    public static WatchRunnable buildWatchService(String watchedPath, IDirUtils dealProcessor) throws Exception {
        return buildWatchService(watchedPath, dealProcessor, null);
    }

    /**
     * 返回一个目录变更的监控器
     *
     * @param watchedPath be watched path
     * @param waitTime    监控时间间隙 (millis)
     * @return
     * @throws Exception
     */
    public static WatchRunnable buildWatchService(String watchedPath, IDirUtils dealProcessor, final Long waitTime) throws Exception {
        if (StrUtils.isEmpty(watchedPath) || !makeSureExist(watchedPath, false)) {
            _log.error("Path '{}' is a invalid path!", watchedPath);
            return null;
        }
        _log.debug("Starting build watch service...");
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get(watchedPath).register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
        _log.debug("Finished build watch service, and ready for watching...");
        return new WatchRunnable(watchService, dealProcessor, waitTime);
    }

    /**
     * Make sure file or directory exist
     *
     * @param path   be checked path
     * @param isFile true:  File
     *               false: Directory
     * @return isExist
     */
    public static boolean makeSureExist(final String path, final boolean isFile) {
        _log.debug("Path: {}, isFile: {}", path, isFile);
        if (StrUtils.isEmpty(path)) return false;
        File file;
        if (!(file = new File(path)).exists()) {
            if (isFile)
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    _log.error("Cannot create new file, because {}", e.getMessage());
                    return false;
                }
            else return file.mkdir();
        }
        return true;
    }

    /**
     * 处理 监控文件夹 的事件
     *
     * @param event
     */
    public void dealWithEvent(WatchEvent<?> event) {
        // could expand more processes here
        _log.info(event.context() + ":\t " + event.kind() + " event.");
    }

    /**
     * 监控线程 (主要为了多线程，能安全地 stop)
     */
    public static class WatchRunnable implements Runnable {
        private IDirUtils dealProcessor;
        private Long waitTime;
        private WatchService watchService;

        /**
         * controller for thread stops safely
         */
        private boolean isRunning = true;

        public WatchRunnable(WatchService watchService, IDirUtils dealProcessor, Long waitTime) {
            this.dealProcessor = dealProcessor;
            this.waitTime = waitTime;
            this.watchService = watchService;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            WatchKey key = null;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                _log.error("WatchService is error, because {}", e.getMessage());
            }
            if (key == null) return;
            IDirUtils dirUtil = dealProcessor == null ? new DirUtils() : dealProcessor;
            while (true) {
                if (!isRunning) return;
                if (waitTime != null && waitTime > 0)
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        _log.error("Thread sleep error, because {}", e.getMessage());
                    }
                if (!key.reset()) break;
                key.pollEvents().forEach(dirUtil::dealWithEvent);
            }
        }
    }
}