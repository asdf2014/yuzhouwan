package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Ip Utils
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

    private static final String PING_PREFIX = "ping -c 1 ";

    /**
     * The current host IP address is the IP address from the device.
     */
    private static List<String> currentHostIpAddress;

    /**
     * 检查 IP地址是否是 合法的
     *
     * @param ip
     * @return
     */
    public static boolean checkValid(final String ip) {
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
        if (StrUtils.isEmpty(url)) return null;
        int len = url.split("/").length;
        Matcher m;
        if (len < 3) {
            _log.error("URL[{}] is invalid!", url);
            return null;
        } else if (len > 3) {
            m = EXTRACT_DOMAIN_WITH_SUB_PATH.matcher(url);
            //这里必须先 find，才能 group取到值
            if (m.find()) return m.group(0);
        } else {
            if (!url.endsWith("/")) {
                m = EXTRACT_DOMAIN_SIMPLE.matcher(url);
            } else {
                m = EXTRACT_DOMAIN_SIMPLE_END_WITH_TAIL.matcher(url);
            }
            if (m.find()) return m.group(0);
        }
        return null;
    }

    /**
     * 获得 url的子路径
     *
     * @param url
     * @return
     */
    public static String getTailFromURL(String url) {
        String domain = extractDomain(url);
        return StrUtils.isEmpty(domain) ? null : StrUtils.cutMiddleStr(url, domain).substring(1);
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
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.valueOf((ipAddress >>> 24)));
        strBuilder.append(".");
        strBuilder.append(String.valueOf((ipAddress & 0x00FFFFFF) >>> 16));
        strBuilder.append(".");
        strBuilder.append(String.valueOf((ipAddress & 0x0000FFFF) >>> 8));
        strBuilder.append(".");
        strBuilder.append(String.valueOf((ipAddress & 0x000000FF)));
        return strBuilder.toString();
    }

    /**
     * Convert IP Address into Int
     *
     * @param ipAddress
     * @return
     */
    public static Integer ip2int(String ipAddress) {

        Inet4Address a;
        try {
            a = (Inet4Address) InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            _log.error("{}", ExceptionUtils.errorInfo(e));
            return null;
        }
        byte[] b = a.getAddress();
        return ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);
    }

    /**
     * 检查 ipAddress 是否在 range 范围内
     *
     * @param ipAddress
     * @param range
     * @return
     */
    public static Boolean checkIPRange(final String ipAddress, final String range) {

        if (StrUtils.isEmpty(range) || StrUtils.isEmpty(ipAddress) || !range.contains("/")) return null;
        String[] rangeArray = range.split("/");
        if (rangeArray.length != 2) return null;
        if (StrUtils.isEmpty(rangeArray[0]) || StrUtils.isEmpty(rangeArray[1])) return null;
        String rangeIp;
        if (!checkValid(rangeIp = rangeArray[0]) || !checkValid(ipAddress)) return null;
        Integer subnet;     // 10.1.1.0/24
        if ((subnet = ip2int(rangeIp)) == null) return false;
        Integer ip;         // 10.1.1.99
        if ((ip = ip2int(ipAddress)) == null) return false;

        // Create bitmask to clear out irrelevant bits. For 10.1.1.0/24 this is
        // 0xFFFFFF00 -- the first 24 bits are 1's, the last 8 are 0's.
        //
        //     -1        == 0xFFFFFFFF
        //     32 - bits == 8
        //     -1 << 8   == 0xFFFFFF00
        int mask = -1 << (32 - Integer.parseInt(rangeArray[1]));

        // IP address is in the subnet.
        return (subnet & mask) == (ip & mask);
    }

    /**
     * Get ip from url.
     *
     * @param url
     * @return
     */
    public static String getIPFromURL(String url) {
        try {
            return Inet4Address.getByName(new URL(url).getHost()).getHostAddress();
        } catch (Exception e) {
            _log.error("{}", ExceptionUtils.errorInfo(e));
            return null;
        }
    }

    /**
     * check ip is reachable
     *
     * @param ipAddress
     * @return
     */
    public static Boolean isReachable(final String ipAddress) {
        try {
            return InetAddress.getByName(ipAddress).isReachable(10000);
        } catch (IOException e) {
            _log.error("{}", ExceptionUtils.errorInfo(e));
        }
        return null;
    }

    public static boolean ping(final String ipAddress) {
        try {
            return Runtime.getRuntime().exec(PING_PREFIX.concat(ipAddress)).waitFor(10000, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            _log.error("{}", ExceptionUtils.errorInfo(e));
            return false;
        }
    }

    /**
     * @return the current environment's IP address, taking into account the Internet connection to any of the available
     * machine's Network interfaces. Examples of the outputs can be in octats or in IPV6 format.
     * <pre>
     *         ==> wlan0
     *
     *         fec0:0:0:9:213:e8ff:fef1:b717%4
     *         siteLocal: true
     *         isLoopback: false isIPV6: true
     *         130.212.150.216 <<<<<<<<<<<------------- This is the one we want to grab so that we can.
     *         siteLocal: false                          address the DSP on the network.
     *         isLoopback: false
     *         isIPV6: false
     *
     *         ==> lo
     *         0:0:0:0:0:0:0:1%1
     *         siteLocal: false
     *         isLoopback: true
     *         isIPV6: true
     *         127.0.0.1
     *         siteLocal: false
     *         isLoopback: true
     *         isIPV6: false
     *  </pre>
     */
    public static List<String> getCurrentEnvironmentNetworkIp() {
        if (currentHostIpAddress == null || currentHostIpAddress.isEmpty()) {
            currentHostIpAddress = new LinkedList<>();
            try {
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                NetworkInterface ni;
                byte[] hardware;
                Enumeration<InetAddress> address;
                while (netInterfaces.hasMoreElements()) {
                    ni = netInterfaces.nextElement();
                    hardware = ni.getHardwareAddress();
                    if (!ni.isUp() || ni.isVirtual() || hardware == null || hardware.length == 0) continue;
                    address = ni.getInetAddresses();
                    InetAddress inetAddress;
                    while (address.hasMoreElements()) {
                        inetAddress = address.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()
                                && !inetAddress.getHostAddress().contains(":"))
                            currentHostIpAddress.add(inetAddress.getHostAddress());
                    }
                }
            } catch (SocketException e) {
                _log.error("{}", ExceptionUtils.errorInfo(e));
                throw new RuntimeException(e);
            }
        }
        return currentHostIpAddress;
    }
}
