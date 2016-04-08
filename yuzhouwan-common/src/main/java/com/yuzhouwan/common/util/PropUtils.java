package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Properties Util
 *
 * @author Benedict Jin
 * @since 2016/4/8 0030
 */
public class PropUtils {

    private static final Logger _log = LoggerFactory.getLogger(PropUtils.class);
    private Properties properties;

    public PropUtils(String confPath) {
        File confFile = new File(confPath);
        if (!confFile.exists())
            return;
        FileInputStream fis;
        try {
            fis = new FileInputStream(confFile);
        } catch (FileNotFoundException e) {
            _log.error(e.getMessage());
            return;
        }
        Properties p;
        try {
            p = new Properties();
            p.load(fis);
        } catch (IOException e) {
            _log.error(e.getMessage());
            return;
        }
        this.properties = p;
    }

    public String getProperty(String key) {
        if (this.properties == null)
            throw new RuntimeException("Properties is not valid!!");
        return this.properties.getProperty(key);
    }

}
