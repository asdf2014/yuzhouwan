package com.yuzhouwan.common.util;

import com.yuzhouwan.common.dir.DirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Jar Utils
 *
 * @author Benedict Jin
 * @since 2016/4/20
 */
public final class JarUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JarUtils.class);

    private static final String SUFFIX_JAR = ".jar";
    private static final String LIB_PATH = DirUtils.getLibPathInWebApp();
    private static final String CLASSES_PATH = DirUtils.getTestClassesPath();
    private static final Properties p = new Properties();
    private static volatile JarUtils instance;
    private static final String PROP_PATH = PropUtils.getInstance().getPropertyInternal("prop.path");

    private JarUtils() {
    }

    public static JarUtils getInstance() {
        if (instance == null) {
            synchronized (PropUtils.class) {
                if (instance == null) {
                    init();
                    instance = new JarUtils();
                }
            }
        }
        return instance;
    }

    private static void init() {
        List<String> jarPaths;
        try {
            // $PROJECT_BASE_PATH/*.jar
            String projectJarPath = PropUtils.getInstance().getPropertyInternal("project.jar.path");
            if (!StrUtils.isEmpty(projectJarPath) && projectJarPath.endsWith(SUFFIX_JAR)) {
                loadPropsWithinJar(projectJarPath);
            }

            // /classes/lib/*.jar
            LOGGER.debug("CLASSES_PATH is {}", CLASSES_PATH);
            if (!StrUtils.isEmpty(CLASSES_PATH)) {
                if ((jarPaths = DirUtils.findPath(CLASSES_PATH, "lib", SUFFIX_JAR, false)) != null
                        && jarPaths.size() > 0)
                    for (String jarFile : jarPaths) {
                        jarPaths = DirUtils.findPath(CLASSES_PATH, "classes", jarFile.substring(1), false);
                        if (jarPaths != null && jarPaths.size() > 0) scanDirWithinJar(jarPaths.get(0));
                    }
            }

            // /lib/*.jar
            LOGGER.debug("LIB_PATH is {}", LIB_PATH);
            if (!StrUtils.isEmpty(LIB_PATH)) {
                if ((jarPaths = DirUtils.findPath(LIB_PATH, "lib", SUFFIX_JAR, false)) != null && jarPaths.size() > 0)
                    for (String jarFile : jarPaths) {
                        //如果是 webApp，这里需要改为 WEB-INF; 否则是 target (supported by profile in maven)
                        jarPaths = DirUtils.findPath(
                            LIB_PATH,
                            PropUtils.getInstance().getPropertyInternal("lib.path"),
                            jarFile.substring(1), false
                        );
                        if (jarPaths != null && jarPaths.size() > 0) {
                            scanDirWithinJar(jarPaths.get(0));
                        }
                    }
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        LOGGER.debug("The number of Properties in Jar is {}.", p.keySet().size());
        for (Object key : p.keySet()) {
            LOGGER.debug("{} : {}", key, p.get(key));
        }
    }

    /**
     * 从 Jar 内部 遍历配置文件，并加载.
     */
    private static void scanDirWithinJar(String jarPath) throws Exception {
        //如果是 webApp，这里需要是改为 ".." + JAR_PATH；否则，直接用 JAR_PATH (supported by profile in maven)
        String prefix = PropUtils.getInstance().getPropertyInternal("prefix.path.for.scan.dir.with.jar");
        String sourcePath = (StrUtils.isEmpty(StrUtils.isEmpty(prefix) ? "" : prefix)
            ? DirUtils.PROJECT_BASE_PATH.concat("/") : "").concat(jarPath);
        LOGGER.debug("SourcePath: {}", sourcePath);
        URL sourceUrl = JarUtils.class.getClassLoader().getResource(sourcePath);
        if (sourceUrl == null && StrUtils.isEmpty(sourcePath)) {
            if (!StrUtils.isEmpty(sourcePath)) {
                sourcePath = PropUtils.getInstance().getPropertyInternal("file.source.url.prefix").concat(sourcePath);
                LOGGER.debug("SourcePath: {}", sourcePath);
                sourceUrl = new URL(sourcePath);
            }
        }
        if (sourceUrl == null) {
            return;
        }
        LOGGER.debug("Jar Path: {}", sourceUrl.getPath());
        loadPropsWithinJar(sourceUrl);
    }

    public static void loadPropsWithinJar(String jarPath) throws Exception {
        // file:/
        String sysFilePrefix = PropUtils.getInstance().getPropertyInternal("file.source.url.prefix");
        LOGGER.debug("System File Url Prefix: {}", sysFilePrefix);
        if (!StrUtils.isEmpty(sysFilePrefix)) {
            loadPropsWithinJar(new URL(sysFilePrefix.concat(jarPath)));
        }
    }

    public static void loadPropsWithinJar(URL sourceUrl) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Load Properties Within Jar File: {}", sourceUrl.getPath());
        }
        try (ZipInputStream zip = new ZipInputStream(sourceUrl.toURI().toURL().openStream())) {
            if (zip.available() == 0) {
                throw new RuntimeException(sourceUrl.getPath().concat(" is not exist or cannot be available!!"));
            }
            ZipEntry e;
            String name;
            while ((e = zip.getNextEntry()) != null) {
                if (!StrUtils.isEmpty(name = e.getName()) && name.startsWith(PROP_PATH)) {
                    if (StrUtils.isEmpty(StrUtils.cutStartStr(name, PROP_PATH))) {
                        continue;
                    }
                    p.load(JarUtils.class.getResourceAsStream("/".concat(name)));
                    for (Object key : p.keySet()) {
                        LOGGER.debug("JarUtils k-v: <{} = {}>", key, p.get(key));
                    }
                }
                LOGGER.debug("Properties File name is {}", name);
            }
        }
    }

    /**
     * 判断某个 Class 是否是 Project 内的，还是外部依赖的 jar 里的.
     *
     * @param clazz Class
     * @return isProjectJar
     */
    public static boolean isProjectJar(final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        try {
            return !new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI())
                .getName()
                .endsWith(SUFFIX_JAR);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
            return true;
        }
    }

    public String getProperty(String key) {
        if (p == null) {
            return null;
        }
        return p.getProperty(key);
    }
}
