package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
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

    private static final Logger _log = LoggerFactory.getLogger(IpUtils.class);

    private static final Pattern IP_ADDRESS_IS_VALID = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static final Pattern EXTRACT_DOMAIN_WITH_SUB_PATH = Pattern.compile("(?<=//).*?(?=/)");
    private static final Pattern EXTRACT_DOMAIN_SIMPLE = Pattern.compile("(?<=//).*");
    private static final Pattern EXTRACT_DOMAIN_SIMPLE_END_WITH_TAIL = Pattern.compile("(?<=//).*(?=/)");

    /**
     * 检查 IP地址是否是 合法的
     *
     * @param ip
     * @return
     */
    public static boolean checkValid(String ip) {
        return IP_ADDRESS_IS_VALID.matcher(ip).matches();
    }

    /**
     * 移除 /32的尾巴
     *
     * @param ip
     * @return
     */
    public static String removeTail32(String ip) {
        return !StrUtils.isEmpty(ip) && ip.endsWith("/32") ? ip.substring(0, ip.length() - 3) : ip;
    }

    /**
     * 抽取域名主干部分
     *
     * @param url
     * @return
     */
    public static String extractDomain(String url) {
        if (StrUtils.isEmpty(url))
            return null;
        int len = url.split("/").length;
        Matcher m;
        if (len < 3) {
            _log.error("URL[{}] is invalid!", url);
            return null;
        } else if (len > 3) {
            m = EXTRACT_DOMAIN_WITH_SUB_PATH.matcher(url);
            //这里必须先 find，才能 group取到值
            if (m.find()) {
                return m.group(0);
            }
        } else {
            if (!url.endsWith("/")) {
                m = EXTRACT_DOMAIN_SIMPLE.matcher(url);
            } else {
                m = EXTRACT_DOMAIN_SIMPLE_END_WITH_TAIL.matcher(url);
            }
            if (m.find()) {
                return m.group(0);
            }
        }
        return null;
    }

    /**
     * Convert IP Address into Long
     *
     * @param ipAddress
     * @return
     */
    public static long ip2long(String ipAddress) {
        long[] ip = new long[4];
        int i = 0;
        for (String ipStr : ipAddress.split("\\.")) {
            ip[i] = Long.parseLong(ipStr);
            i++;
        }
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * Convert Long into IP Address
     *
     * @param ipAddress
     * @return
     */
    public static String long2ip(Long ipAddress) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipAddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x000000FF)));
        return sb.toString();
    }

}
