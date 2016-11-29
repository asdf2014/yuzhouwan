package com.yuzhouwan.common.util;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Exception Utils
 *
 * @author Benedict Jin
 * @since 2016/11/24
 */
public class ExceptionUtils {

    public static String errorInfo(final Exception e) {
        return String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
    }
}
