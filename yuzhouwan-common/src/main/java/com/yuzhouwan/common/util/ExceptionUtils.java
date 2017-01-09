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

    public static String errorInfo(Exception e) {
        return errorInfo(e, null);
    }

    public static String errorInfo(Exception e, String detail) {
        if (e == null) return null;
        Class<?> clazz;
        return String.format("%s: %s%s", (clazz = e.getClass()) == null ? "" : clazz.getSimpleName(),
                e.getMessage(), StrUtils.isEmpty(detail) ? "" : ", Detail: ".concat(detail));
    }
}