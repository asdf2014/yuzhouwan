package com.yuzhouwan.site.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
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

    private static final String confPath = "conf/Prop.properties";

    public void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(confPath);
        properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            _log.error(e.getMessage());
        }
    }

    public PropUtils() {
    }

    public String getProperty(String key) {
        if (properties == null)
            throw new RuntimeException("Properties is not valid!!");
        return properties.getProperty(key);
    }

    @Test
    public void test() {
        System.out.println(properties.getProperty("site.name"));
    }
}