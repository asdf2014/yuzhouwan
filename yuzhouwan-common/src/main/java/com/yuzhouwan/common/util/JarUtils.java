package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Jar Utils
 *
 * @author Benedict Jin
 * @since 2016/4/20 0030
 */
public class JarUtils {

    private static final Logger _log = LoggerFactory.getLogger(JarUtils.class);

    private static final String PROP_PATH = PropUtils.getInstance().getPropertyInternal("prop.path");
    private static final String LIB_PATH = DirUtils.getLibPathInWebApp();
    private static final String CLASSES_PATH = DirUtils.getTestClassesPath();
    private static Properties properties = new Properties();

    private static volatile JarUtils instance;

    private JarUtils() {
    }

    public static JarUtils getInstance() {
        if (instance == null)
            synchronized (PropUtils.class) {
                if (instance == null) {
                    init();
                    instance = new JarUtils();
                }
            }
        return instance;
    }

    private static void init() {
        List<String> jarPaths;
        try {
            /**
             * $PROJECT_BASE_PATH/*.jar
             */
            String projectJarPath = PropUtils.getInstance().getPropertyInternal("project.jar.path");
            if (!StrUtils.isEmpty(projectJarPath) && projectJarPath.endsWith(".jar"))
                loadPropsWithinJar(projectJarPath);

            /**
             * /classes/lib/*.jar
             */
            _log.debug("CLASSES_PATH is {}", CLASSES_PATH);
            String jarPath;
            if (!StrUtils.isEmpty(CLASSES_PATH)) {
                jarPaths = DirUtils.findPath(CLASSES_PATH, ".jar", false, "lib");
                if (jarPaths != null && jarPaths.size() > 0) {
                    for (String jarFile : jarPaths) {
                        jarFile = jarFile.substring(1);
                        jarPaths = DirUtils.findPath(CLASSES_PATH, jarFile, false, "classes");
                        if (jarPaths != null && jarPaths.size() > 0) {
                            jarPath = jarPaths.get(0);
                            scanDirWithinJar(jarPath);
                        }
                    }
                }
            }

            /**
             * /lib/*.jar
             */
            _log.debug("LIB_PATH is {}", LIB_PATH);
            if (!StrUtils.isEmpty(LIB_PATH)) {
                jarPaths = DirUtils.findPath(LIB_PATH, ".jar", false, "lib");
                if (jarPaths != null && jarPaths.size() > 0) {
                    for (String jarFile : jarPaths) {
                        jarFile = jarFile.substring(1);
                        //如果是 webApp，这里需要改为 WEB-INF; 否则是 target (supported by profile in maven)
                        jarPaths = DirUtils.findPath(LIB_PATH, jarFile, false,
                                PropUtils.getInstance().getPropertyInternal("lib.path"));
                        if (jarPaths != null && jarPaths.size() > 0) {
                            jarPath = jarPaths.get(0);
                            scanDirWithinJar(jarPath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }
        _log.debug("The number of Properties in Jar is {}.", properties.keySet().size());
        for (Object key : properties.keySet()) {
            _log.debug("{} : {}", key, properties.get(key));
        }
    }

    /**
     * 从 Jar内部 遍历配置文件，并加载
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    private static void scanDirWithinJar(String jarPath) throws Exception {
        //如果是 webApp，这里需要是改为 ".." + JAR_PATH；否则，直接用 JAR_PATH (supported by profile in maven)
        String prefix = PropUtils.getInstance().getPropertyInternal("prefix.path.for.scan.dir.with.jar");
        String sourcePath = (StrUtils.isEmpty(StrUtils.isEmpty(prefix) ? "" : prefix) ?
                DirUtils.PROJECT_BASE_PATH.concat("/") : "").concat(jarPath);
        _log.debug("SourcePath: {}", sourcePath);
        URL sourceUrl = JarUtils.class.getClassLoader().getResource(sourcePath);
        if (sourceUrl == null && StrUtils.isEmpty(sourcePath)) {
            if (!StrUtils.isEmpty(sourcePath)) {
                sourcePath = PropUtils.getInstance().getPropertyInternal("file.source.url.prefix").concat(sourcePath);
                _log.debug("SourcePath: {}", sourcePath);
                sourceUrl = new URL(sourcePath);
            }
            if (sourceUrl == null)
                return;
        }
        _log.debug("Jar Path: {}", sourceUrl.getPath());
        loadPropsWithinJar(sourceUrl);
    }

    public static void loadPropsWithinJar(String jarPath) throws Exception {
        // file:/
        String sysFilePrefix = PropUtils.getInstance().getPropertyInternal("file.source.url.prefix");
        _log.debug("System File Url Prefix: {}", sysFilePrefix);
        if (!StrUtils.isEmpty(sysFilePrefix))
            loadPropsWithinJar(new URL(sysFilePrefix.concat(jarPath)));
    }

    public static void loadPropsWithinJar(URL sourceUrl) throws Exception {
        _log.debug("Load Properties Within Jar File: {}", sourceUrl.getPath());
        try (ZipInputStream zip = new ZipInputStream(sourceUrl.toURI().toURL().openStream())) {
            if (zip.available() == 0)
                throw new RuntimeException(sourceUrl.getPath().concat(" is not exist or cannot be available!!"));

            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                _log.debug("Properties File name is {}", name);
                if (!StrUtils.isEmpty(name) && name.startsWith(PROP_PATH)) {
                    if (StrUtils.isEmpty(StrUtils.cutStartStr(name, PROP_PATH)))
                        continue;
                    properties.load(JarUtils.class.getResourceAsStream("/".concat(name)));
                    for (Object key : properties.keySet()) {
                        _log.debug("JarUtils k-v: <{} = {}>", key, properties.get(key));
                    }
                }
            }
        }
    }

    public String getProperty(String key) {
        if (properties == null)
            return null;
        return properties.getProperty(key);
    }

    /**
     * 判断某个 Class是否是 Project内的，还是外部依赖的 jar里的
     *
     * @param clazz
     * @return
     */
    public static boolean isProjectJar(final Class<?> clazz) {
        final URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        try {
            return !new File(location.toURI()).getName().endsWith(".jar");
        } catch (Exception ignored) {
            _log.error("{}", ignored);
            return true;
        }
    }
}