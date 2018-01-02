package com.yuzhouwan.common.util;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Exception Utils
 *
 * @author Benedict Jin
 * @since 2016/11/24
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static String errorInfo(Exception e) {
        return errorInfo(e, null);
    }

    public static String errorInfo(Exception e, String detail) {
        if (e == null) return null;
        Class<?> clazz = e.getClass();
        String msg = e.getMessage();
        return String.format("%s: %s%s", clazz == null ? "" : clazz.getSimpleName(),
                StrUtils.isEmpty(msg) ? "[No More Detail Info]" : msg,
                StrUtils.isEmpty(detail) ? "" : ", Detail: ".concat(detail));
    }
}
