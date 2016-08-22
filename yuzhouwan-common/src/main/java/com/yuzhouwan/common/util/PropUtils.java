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
    private static Properties properties = new Properties();

    private static volatile PropUtils instance;

    private PropUtils(List<String> confPathList) {

        for (String confPath : confPathList) {
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
                continue;
            }
        }
    }

    public static PropUtils getInstance() {
        if (instance == null)
            synchronized (PropUtils.class) {
                if (instance == null)
                    instance = new PropUtils(DirUtils.findPath(DirUtils.getClassesPath(), ".properties", true, "prop"));
            }
        return instance;
    }

    public String getProperty(String key) {
        if (properties == null)
            throw new RuntimeException("Properties is not valid!!");
        return properties.getProperty(key);
    }

}
