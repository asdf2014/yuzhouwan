package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Ip Util Tester
 *
 * @author Benedict Jin
 * @since 2016/4/7
 */
public class IpUtilsTest {

    @Test
    public void checkValidTest() throws Exception {
        assertEquals(true, IpUtils.checkValid("1.1.1.1"));
        assertEquals(true, IpUtils.checkValid("113.12.83.4"));
        assertEquals(false, IpUtils.checkValid("313.12.83.4"));
    }

    @Test
    public void removeTail32Test() throws Exception {
        assertEquals("1.1.1.1", IpUtils.removeTail32("1.1.1.1/32"));
        assertEquals("113.12.83.4", IpUtils.removeTail32("113.12.83.4/32"));
    }

    @Test
    public void extractDomainTest() throws Exception {
        assertEquals("yuzhouwan.com", IpUtils.extractDomain("http://yuzhouwan.com"));
        assertEquals("yuzhouwan.com", IpUtils.extractDomain("http://yuzhouwan.com/"));
        assertEquals("yuzhouwan.com", IpUtils.extractDomain("http://yuzhouwan.com/subpath/welcome.html"));
        assertEquals(null, IpUtils.extractDomain("http:/yuzhouwan.com"));

        assertEquals(null, IpUtils.extractDomain(""));
        assertEquals(null, IpUtils.extractDomain(null));
    }

    @Test
    public void getTailFromURLTest() throws Exception {
        assertEquals("group1/M00/00/00/oYYBAFd06DSAQ5gwAAIIUQRZ1_c574.pdf", IpUtils.getTailFromURL("http://192.168.112.171:9090/group1/M00/00/00/oYYBAFd06DSAQ5gwAAIIUQRZ1_c574.pdf"));
    }

    @Test
    public void testLongAndIp() throws Exception {
        String ipAddressStr = "192.168.5.11";
        Long ipAddressLong = 3232236811L;
        assertEquals(ipAddressStr, IpUtils.long2ip(IpUtils.ip2long(ipAddressStr)));
        if (ipAddressLong == IpUtils.ip2long(IpUtils.long2ip(ipAddressLong))) {
            System.out.println(IpUtils.long2ip(ipAddressLong));
            System.out.println(true);
        }
    }

    @Test
    public void checkIP2IntTest() throws Exception {
        int ip = IpUtils.ip2int("10.1.1.0");
        assertEquals(167837952, ip);
        assertEquals(0x0A010100, ip);
    }

    @Test
    public void checkIPRangeTest() throws Exception {
        assertEquals(null, IpUtils.checkIPRange("", ""));
        assertEquals(null, IpUtils.checkIPRange("", "10.1.1.0/24"));
        assertEquals(null, IpUtils.checkIPRange("10.1.1.1", "10.1.1.0/"));
        assertEquals(null, IpUtils.checkIPRange("10.1.1.1", "/24"));

        assertEquals(null, IpUtils.checkIPRange("9999.1.1.1", "10.1.1.0/24"));

        assertEquals(true, IpUtils.checkIPRange("10.1.1.1", "10.1.1.0/24"));
        assertEquals(false, IpUtils.checkIPRange("10.1.2.1", "10.1.1.0/24"));

        assertEquals(false, IpUtils.checkIPRange("113.12.83.3", "113.12.83.4/32"));
        assertEquals(true, IpUtils.checkIPRange("113.12.83.4", "113.12.83.4/32"));

        String aim = "113.12.83.4";
        String ranges = "113.12.83.4/32,113.12.83.5/32,116.1.239.35/32";
        boolean result = false;
        if (ranges.contains(",")) {
            for (String range : ranges.split(",")) {
                Boolean isInRange = IpUtils.checkIPRange(aim, range);
                if (isInRange != null && isInRange) {
                    result = true;
                    break;
                }
            }
        }
        assertEquals(true, result);
    }

    //    @Test
    public void getIPFromURLTest() throws Exception {

        assertEquals(null, IpUtils.getIPFromURL("baidu.com"));
        assertEquals(null, IpUtils.getIPFromURL("www.baidu.com"));
        System.out.println(IpUtils.getIPFromURL("http://www.baidu.com/"));
        System.out.println(IpUtils.getIPFromURL("https://www.aliyun.com/"));

        System.out.println(IpUtils.getIPFromURL("http://180.97.33.107"));
        System.out.println(IpUtils.getIPFromURL("ftp://11.22.33.44"));
        System.out.println(IpUtils.getIPFromURL("http://www.baidu.com"));
    }

    //    @Test
    public void isReachableTest() throws Exception {

        assertEquals(true, IpUtils.isReachable("127.0.0.1"));
        //stop following test for performance
//        System.out.println(IpUtils.isReachable("192.168.1.101"));
//        System.out.println(IpUtils.isReachable(IpUtils.getIPFromURL("http://www.baidu.com")));
    }

    @Test
    public void pingTest() throws Exception {
        System.out.println(IpUtils.ping("127.0.0.1"));
    }

    //    @Test
    public void getCurrentEnvironmentNetworkIpTest() throws Exception {
        List<String> ips = IpUtils.getCurrentEnvironmentNetworkIp();
        for (String ip : ips) {
            System.out.println(ip);
            System.out.println(IpUtils.isReachable(ip));
            System.out.println(IpUtils.ping(ip));
        }
    }
}
