package com.yuzhouwan.hacker.property;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：annotation.property
 *
 * @author Benedict Jin
 * @since 2015/12/12 0011
 */
public class Log4jProperty {

    private static final Logger LOG = Logger.getLogger(Log4jProperty.class);
    private static final String LOG4J_PROPERTIES_PATH_POSTFIX = "log4j.properties";

    public static void main(String[] args) throws MalformedURLException {

        /**
         * Run jar with crontab:
         * log4j:ERROR Ignoring configuration file [/root//log4j.properties]
         *
         * Solve:
         * path=/home/hadoop/label/labelToEs
         * source /etc/profile
         * source /root/.bash_profile
         * cd $path
         * java -jar $path/initToEs.jar
         */
        URL url = Log4jProperty.class.getResource("/");
        LOG.info(url);

        String path = url.toString().concat(LOG4J_PROPERTIES_PATH_POSTFIX);
        LOG.info(path);

        /**
         * Auto
         */
        PropertyConfigurator.configure(new URL(path));
    }
}
