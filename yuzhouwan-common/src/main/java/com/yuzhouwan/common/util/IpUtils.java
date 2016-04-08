package com.yuzhouwan.common.util;

import java.util.regex.Pattern;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Ip Util
 *
 * @author Benedict Jin
 * @since 2016/4/7 0030
 */
public class IpUtils {

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean checkValid(String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public static String removeTail32(String ip) {
        return !StrUtils.isEmpty(ip) && ip.endsWith("/32") ? ip.substring(0, ip.length() - 3) : ip;
    }

}
