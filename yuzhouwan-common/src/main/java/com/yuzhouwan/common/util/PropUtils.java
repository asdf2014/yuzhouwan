package com.yuzhouwan.common.util;

import com.yuzhouwan.common.dir.DirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Properties Utils
 *
 * @author Benedict Jin
 * @since 2016/4/8
 */
public class PropUtils {

    private static final Logger _log = LoggerFactory.getLogger(PropUtils.class);
    private static final Properties properties = new Properties();

    private static volatile PropUtils instance;

    private PropUtils(List<String> confPathList) {
        if (confPathList == null || confPathList.size() == 0) return;
        File confFile;
        for (String confPath : confPathList) {
            if (StrUtils.isEmpty(confPath) || !(confFile = new File(confPath)).exists()) continue;
            try (FileInputStream fis = new FileInputStream(confFile)) {
                properties.load(fis);
            } catch (Exception e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        }
    }

    public static PropUtils getInstance() {
        if (instance == null)
            synchronized (PropUtils.class) {
                if (instance == null) {
                    List<String> prop = DirUtils.findPath(DirUtils.getClassesPath(), "prop", ".properties", true);
                    if (prop == null || prop.size() == 0)
                        prop = DirUtils.findPath(DirUtils.getProjectBasicPath(), "prop", ".properties", true);
                    instance = new PropUtils(prop);
                }
            }
        return instance;
    }

    /**
     * 1. Only load properties that located within project
     * 2. External properties could rewrite exists k-v in PropUtils
     *
     * @param key
     * @param withinJar
     * @return
     */
    public String getProperty(String key, boolean withinJar) {
        if (StrUtils.isEmpty(key)) return null;
        if (properties == null) throw new RuntimeException("Properties is not valid!!");
        String value = properties.getProperty(key);
        if (withinJar && StrUtils.isEmpty(value)) {
            String valueJar = JarUtils.getInstance().getProperty(key);
            return StrUtils.isEmpty(valueJar) ? value : valueJar;
        }
        return value;
    }

    /**
     * Avoid recursion in JarUtils' initializtion
     *
     * @param key
     * @return
     */
    public String getPropertyInternal(String key) {
        return getProperty(key, false);
    }

    public String getProperty(String key) {
        return getProperty(key, true);
    }
}