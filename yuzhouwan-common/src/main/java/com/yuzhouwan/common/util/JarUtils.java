package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Ip Util
 *
 * @author Benedict Jin
 * @since 2016/4/20 0030
 */
public class JarUtils {

    private static final Logger _log = LoggerFactory.getLogger(JarUtils.class);
    private static final String JAR_NAME = PropUtils.getInstance().getProperty("jar.name");
    private static String JAR_PATH;
    private static final String PROP_PATH = PropUtils.getInstance().getProperty("prop.path");
    private static Properties properties = new Properties();

    static {
        try {
            List<String> jarPaths = DirUtils.findPath(DirUtils.getClassesPath(), JAR_NAME, false, "classes");
            if (jarPaths != null && jarPaths.size() > 0) {
                JAR_PATH = DirUtils.findPath(DirUtils.getClassesPath(), JAR_NAME, false, "classes").get(0);
                scanDirWithinJar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
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
        ZipInputStream zip = new ZipInputStream(
                JarUtils.class.getClassLoader().getResource(JAR_PATH)
                        .toURI().toURL().openStream());
        if (zip == null)
            throw new RuntimeException(JAR_PATH.concat(" is not exist!!"));
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

    public static String getProperty(String key) {
        if (properties == null)
            return null;
        return properties.getProperty(key);
    }
}