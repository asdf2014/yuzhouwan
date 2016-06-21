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
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Jar Util
 *
 * @author Benedict Jin
 * @since 2016/4/20 0030
 */
public class JarUtils {

    private static final Logger _log = LoggerFactory.getLogger(JarUtils.class);
    //    private static final String JAR_NAME = PropUtils.getInstance().getProperty("jar.name");
    private static String JAR_PATH;
    private static final String PROP_PATH = PropUtils.getInstance().getProperty("prop.path");
    private static Properties properties = new Properties();

    private static final String LIB_PATH = DirUtils.getLibPathInWebApp();
    private static final String CLASSES_PATH = DirUtils.getTestClassesPath();

    static {
        try {
            /**
             * /classes/lib/*.jar
             */
            List<String> jarPaths = DirUtils.findPath(CLASSES_PATH, ".jar", false, "lib");
            if (jarPaths != null && jarPaths.size() > 0) {
                for (String jarFile : jarPaths) {
                    jarFile = jarFile.substring(1);
                    jarPaths = DirUtils.findPath(CLASSES_PATH, jarFile, false, "classes");
                    if (jarPaths != null && jarPaths.size() > 0) {
                        JAR_PATH = jarPaths.get(0);
                        scanDirWithinJar();
                    }
                }
            }
            /**
             * /lib/*.jar
             */
            jarPaths = DirUtils.findPath(LIB_PATH, ".jar", false, "lib");
            if (jarPaths != null && jarPaths.size() > 0) {
                for (String jarFile : jarPaths) {
                    jarFile = jarFile.substring(1);
                    //如果是 webApp，这里需要改为 WEB-INF; 否则是 target (supported by profile in maven)
                    jarPaths = DirUtils.findPath(LIB_PATH, jarFile, false,
                            PropUtils.getInstance().getProperty("lib.path"));
                    if (jarPaths != null && jarPaths.size() > 0) {
                        JAR_PATH = jarPaths.get(0);
                        scanDirWithinJar();
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从 Jar内部 遍历配置文件，并加载
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    private static void scanDirWithinJar() throws IOException, URISyntaxException {
        //如果是 webApp，这里需要是改为 ".." + JAR_PATH；否则，直接用 JAR_PATH (supported by profile in maven)
        String prefix = PropUtils.getInstance().getProperty("prefix.path.for.scan.dir.with.jar");
        URL sourceUrl = JarUtils.class.getClassLoader().getResource(
                (StrUtils.isEmpty(prefix) ? "" : prefix).concat(JAR_PATH));
        if (sourceUrl == null)
            return;

        try (ZipInputStream zip = new ZipInputStream(sourceUrl.toURI().toURL().openStream())) {
            if (zip == null || zip.available() == 0)
                throw new RuntimeException(JAR_PATH.concat(" is not exist or cannot be available!!"));

            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if (name.startsWith(PROP_PATH)) {
                    if (StrUtils.isEmpty(StrUtils.cutStartStr(name, PROP_PATH)))
                        continue;
                    _log.debug(name);
                    properties.load(JarUtils.class.getResourceAsStream("/".concat(name)));
                }
            }
        }
    }

    public static String getProperty(String key) {
        if (properties == null)
            return null;
        return properties.getProperty(key);
    }

    /**
     * 判断某个 Class是否是 Project内的，还是外部依赖的 jar里的
     *
     * @param klass
     * @return
     */
    public static boolean isProjectJar(Class<?> klass) {
        final URL location = klass.getProtectionDomain().getCodeSource().getLocation();
        try {
            return !new File(location.toURI()).getName().endsWith(".jar");
        } catch (Exception ignored) {
            _log.error("{}", ignored);
            return true;
        }
    }
}