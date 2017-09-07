package com.yuzhouwan.hacker.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: UnsafeApp
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class UnsafeApp {

    private static final Logger _log = LoggerFactory.getLogger(UnsafeApp.class);

    /**
     * @param args arg0 arg1 arg2
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        _log.debug("This is your application.");
        _log.debug("Args: ");
        for (String arg : args) _log.debug(arg + " ");

        new UnsafeClass();
    }
}

