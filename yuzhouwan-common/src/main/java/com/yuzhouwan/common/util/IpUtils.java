package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuzhouwan.common.util.StrUtils.isEmpty;
import static com.yuzhouwan.common.util.StrUtils.isNotEmpty;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Ip Utils
 *
 * @author Benedict Jin
 * @since 2016/4/7
 */
public final class IpUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(IpUtils.class);

  private static final int REMOVE_TAIL_LENGTH = 3;
  private static final Pattern IP_V4_ADDRESS_IS_VALID = Pattern.compile(
    "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])");
  private static final Pattern IP_V4_ADDRESS_IS_VALID2 = Pattern.compile(
    "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
  private static final Pattern IP_V6_ADDRESS_IS_VALID = Pattern.compile(
    "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:"
      + "[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:)"
      + "{1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"
      + "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]"
      + "{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|"
      + "::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\\\.){3}"
      + "(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|"
      + "(2[0-4]|1?[0-9])?[0-9])\\\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))");
  private static final Pattern EXTRACT_DOMAIN_WITH_SUB_PATH = Pattern.compile("(?<=//).*?(?=/)");
  private static final Pattern EXTRACT_DOMAIN_SIMPLE = Pattern.compile("(?<=//).*");
  private static final Pattern EXTRACT_DOMAIN_SIMPLE_END_WITH_TAIL = Pattern.compile("(?<=//).*(?=/)");
  private static final String PING_PREFIX = "ping -c 1 ";

  /**
   * The current host IP address is the IP address from the device.
   */
  private static volatile List<String> CURRENT_HOST_IP_ADDRESS;

  private IpUtils() {
  }

  /**
   * 检查 IP-v4 地址是否是 合法的.
   */
  public static boolean checkValid(final String ip) {
    return isNotEmpty(ip) && IP_V4_ADDRESS_IS_VALID.matcher(ip).matches();
  }

  /**
   * 检查 IP-v4 地址是否是 合法的.
   */
  public static boolean checkValid2(final String ip) {
    return isNotEmpty(ip) && IP_V4_ADDRESS_IS_VALID2.matcher(ip).matches();
  }

  /**
   * 检查 IP-v6 地址是否是 合法的.
   */
  public static boolean checkValidV6(final String ip) {
    return isNotEmpty(ip) && IP_V6_ADDRESS_IS_VALID.matcher(ip).matches();
  }

  /**
   * 移除 /32 的尾巴.
   */
  public static String removeTail32(String ip) {
    return isNotEmpty(ip) && ip.endsWith("/32") ? ip.substring(0, ip.length() - REMOVE_TAIL_LENGTH) : ip;
  }

  /**
   * 抽取域名主干部分.
   */
  public static String extractDomain(String url) {
    if (isEmpty(url)) {
      return null;
    }
    int len = url.split("/").length;
    Matcher m;
    if (len < 3) {
      LOGGER.error(String.format("URL[%s] is invalid!", url));
      return null;
    } else if (len > 3) {
      // 这里必须先 find，才能 group 取到值
      if ((m = EXTRACT_DOMAIN_WITH_SUB_PATH.matcher(url)).find()) {
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
   * 获得 url 的子路径.
   */
  public static String getTailFromURL(String url) {
    String domain = extractDomain(url);
    return isEmpty(domain) ? null : StrUtils.cutMiddleStr(url, domain).substring(1);
  }

  /**
   * Convert IP Address into Long.
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
   * Convert Long into IP Address.
   */
  public static String long2ip(Long ipAddress) {
    return (ipAddress >>> 24) + "." + ((ipAddress & 0x00FFFFFF) >>> 16) + "."
      + ((ipAddress & 0x0000FFFF) >>> 8) + "." + (ipAddress & 0x000000FF);
  }

  /**
   * Convert IP Address into Int.
   */
  public static Integer ip2int(String ipAddress) {
    Inet4Address a;
    try {
      a = (Inet4Address) InetAddress.getByName(ipAddress);
    } catch (UnknownHostException e) {
      LOGGER.error(ExceptionUtils.errorInfo(e));
      return null;
    }
    byte[] b = a.getAddress();
    return ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);
  }

  /**
   * 检查 ipAddress 是否在 range 范围内.
   */
  public static Boolean checkIPRange(final String ipAddress, final String range) {
    if (isEmpty(range) || isEmpty(ipAddress) || !range.contains("/")) {
      return null;
    }
    String[] rangeArray = range.split("/");
    if (rangeArray.length != 2) {
      return null;
    }
    if (isEmpty(rangeArray[0]) || isEmpty(rangeArray[1])) {
      return null;
    }
    String rangeIp = rangeArray[0];
    if (!checkValid(rangeIp) || !checkValid(ipAddress)) {
      return null;
    }
    Integer subnet = ip2int(rangeIp);     // 10.1.1.0/24
    if (subnet == null) {
      return false;
    }
    Integer ip = ip2int(ipAddress);         // 10.1.1.99
    if (ip == null) {
      return false;
    }

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
   */
  public static String getIPFromURL(String url) {
    try {
      return Inet4Address.getByName(new URL(url).getHost()).getHostAddress();
    } catch (Exception e) {
      LOGGER.error(ExceptionUtils.errorInfo(e));
      return null;
    }
  }

  /**
   * Check ip is reachable.
   */
  public static Boolean isReachable(final String ipAddress) {
    try {
      return InetAddress.getByName(ipAddress).isReachable(10000);
    } catch (IOException e) {
      LOGGER.error(ExceptionUtils.errorInfo(e));
      return null;
    }
  }

  public static boolean ping(final String ipAddress) {
    try {
      return Runtime.getRuntime()
        .exec(PING_PREFIX.concat(ipAddress))
        .waitFor(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOGGER.error(ExceptionUtils.errorInfo(e));
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
  public static List<String> getCurrentEnvironmentNetworkIps() {
    if (CURRENT_HOST_IP_ADDRESS == null || CURRENT_HOST_IP_ADDRESS.isEmpty()) synchronized (IpUtils.class) {
      if (CURRENT_HOST_IP_ADDRESS == null || CURRENT_HOST_IP_ADDRESS.isEmpty())
        try {
          Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
          NetworkInterface ni;
          byte[] hardware;
          Enumeration<InetAddress> address;
          InetAddress inetAddress;
          CURRENT_HOST_IP_ADDRESS = new ArrayList<>();
          while (netInterfaces.hasMoreElements()) {
            ni = netInterfaces.nextElement();
            if (!ni.isUp() || ni.isVirtual()) {
              continue;
            }
            hardware = ni.getHardwareAddress();
            if (hardware == null || hardware.length == 0) {
              continue;
            }
            address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
              inetAddress = address.nextElement();
              if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()
                && !inetAddress.getHostAddress().contains(":"))
                CURRENT_HOST_IP_ADDRESS.add(inetAddress.getHostAddress());
            }
          }
        } catch (SocketException se) {
          LOGGER.error(ExceptionUtils.errorInfo(se));
          throw new RuntimeException(se);
        }
    }
    return CURRENT_HOST_IP_ADDRESS;
  }
}
