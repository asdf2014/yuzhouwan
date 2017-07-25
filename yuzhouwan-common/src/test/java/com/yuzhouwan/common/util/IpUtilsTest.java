package com.yuzhouwan.common.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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

    private static final Logger _log = LoggerFactory.getLogger(IpUtilsTest.class);

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn128M -XX:+AlwaysPreTouch

    [IpUtils.checkValid] Count: 1, Time: 1.751002 ms
    [IpUtils.checkValid] Count: 100, Time: 1.409201 ms
    [IpUtils.checkValid] Count: 10000, Time: 15.800056 ms
    [IpUtils.checkValid] Count: 1000000, Time: 875.6801019999999 ms
    */
    @Test
    public void checkValidTest() throws Exception {
        int base = 0, refactor = 1;
        while (base < refactor) {
            ipValidTest((long) Math.pow(10, base));
            base += 2;
        }
    }

    private void ipValidTest(long len) {
        long begin, end, count = 0, total = 0;
        boolean actual, actual2, actual3;
        while (count < len) {
            begin = System.nanoTime();
            actual = IpUtils.checkValid("1.1.1.1");
            actual2 = IpUtils.checkValid("113.12.83.4");
            actual3 = IpUtils.checkValid("313.12.83.4");
            end = System.nanoTime();
            assertEquals(true, actual);
            assertEquals(true, actual2);
            assertEquals(false, actual3);
            total += (end - begin);
            count++;
        }
        _log.info("[IpUtils.checkValid] Count: {}, Time: {} ms", count, total * Math.pow(10, -6));
    }

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn128M -XX:+AlwaysPreTouch

    [IpUtils.checkValid2] Count: 1, Time: 2.327619 ms
    [IpUtils.checkValid2] Count: 100, Time: 1.503747 ms
    [IpUtils.checkValid2] Count: 10000, Time: 25.263263 ms
    [IpUtils.checkValid2] Count: 1000000, Time: 867.07715 ms
    */
    @Test
    public void checkValidTest2() throws Exception {
        int base = 0, refactor = 1;
        while (base < refactor) {
            ipValidTest2((long) Math.pow(10, base));
            base += 2;
        }
    }

    private void ipValidTest2(long len) {
        long begin, end, count = 0, total = 0;
        boolean actual, actual2, actual3;
        count = total = 0;
        while (count < len) {
            begin = System.nanoTime();
            actual = IpUtils.checkValid2("1.1.1.1");
            actual2 = IpUtils.checkValid2("113.12.83.4");
            actual3 = IpUtils.checkValid2("313.12.83.4");
            end = System.nanoTime();
            assertEquals(true, actual);
            assertEquals(true, actual2);
            assertEquals(false, actual3);
            total += (end - begin);
            count++;
        }
        _log.info("[IpUtils.checkValid2] Count: {}, Time: {} ms", count, total * Math.pow(10, -6));
    }

    @Test
    public void checkValidTestV6() throws Exception {
        assertEquals(true, IpUtils.checkValidV6("2001:0db8:0000:0000:0000:ff00:0042:8329"));
        assertEquals(true, IpUtils.checkValidV6("2001:db8:0:0:0:ff00:42:8329"));
        assertEquals(true, IpUtils.checkValidV6("0000:0000:0000:0000:0000:0000:0000:0001"));
        assertEquals(true, IpUtils.checkValidV6("::1"));
        assertEquals(false, IpUtils.checkValidV6("2001:0db8:0000:0000:0000:ff00:0042:83299"));
        assertEquals(false, IpUtils.checkValidV6("::11111"));
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
        Integer ip2int = IpUtils.ip2int("10.1.1.0");
        Assert.notNull(ip2int);
        int ip = ip2int;
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

    //    @Test
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
