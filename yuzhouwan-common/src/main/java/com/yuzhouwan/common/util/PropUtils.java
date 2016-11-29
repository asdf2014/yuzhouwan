package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Properties Utils
 *
 * @author Benedict Jin
 * @since 2016/4/8 0030
 */
public class PropUtils {

    private static final Logger _log = LoggerFactory.getLogger(PropUtils.class);
    private static final Properties properties = new Properties();

    private static volatile PropUtils instance;

    private PropUtils(List<String> confPathList) {
        if (confPathList == null || confPathList.size() == 0) {
            return;
        }
        for (String confPath : confPathList) {
            if (StrUtils.isEmpty(confPath)) {
                continue;
            }
            File confFile = new File(confPath);
            if (!confFile.exists())
                continue;
            FileInputStream fis;
            try {
                fis = new FileInputStream(confFile);
            } catch (FileNotFoundException e) {
                _log.error(e.getMessage());
                continue;
            }
            try {
                properties.load(fis);
            } catch (IOException e) {
                _log.error(e.getMessage());
            }
        }
    }

    public static PropUtils getInstance() {
        if (instance == null)
            synchronized (PropUtils.class) {
                if (instance == null) {
                    List<String> prop = DirUtils.findPath(DirUtils.getClassesPath(), ".properties", true, "prop");
                    if (prop == null)
                        prop = DirUtils.findPath(DirUtils.getProjectBasicPath(), ".properties", true, "prop");
                    instance = new PropUtils(prop);
                }
            }
        return instance;
    }

    public String getProperty(String key, boolean withinJar) {
        if (StrUtils.isEmpty(key)) {
            return null;
        }
        if (properties == null)
            throw new RuntimeException("Properties is not valid!!");
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
