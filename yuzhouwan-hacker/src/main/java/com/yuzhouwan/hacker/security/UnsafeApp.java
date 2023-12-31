package com.yuzhouwan.hacker.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: UnsafeApp
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class UnsafeApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnsafeApp.class);

    /**
     * @param args arg0 arg1 arg2
     */
    public static void main(String[] args) {
        LOGGER.debug("This is your application.");
        LOGGER.debug("Args: ");
        for (String arg : args) {
            LOGGER.debug(arg + " ");
        }
        new UnsafeClass();
    }
}

