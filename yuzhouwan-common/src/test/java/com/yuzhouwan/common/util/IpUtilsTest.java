package com.yuzhouwan.common.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

import static com.yuzhouwan.common.util.IpUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright @ 2019 yuzhouwan.com
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
    public void checkValidTest() {
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
            actual = checkValid("1.1.1.1");
            actual2 = checkValid("113.12.83.4");
            actual3 = checkValid("313.12.83.4");
            end = System.nanoTime();
            assertTrue(actual);
            assertTrue(actual2);
            assertFalse(actual3);
            total += (end - begin);
            count++;
        }
        _log.info("[checkValid] Count: {}, Time: {} ms", count, total * Math.pow(10, -6));
    }

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn128M -XX:+AlwaysPreTouch

    [checkValid2] Count: 1, Time: 2.327619 ms
    [checkValid2] Count: 100, Time: 1.503747 ms
    [checkValid2] Count: 10000, Time: 25.263263 ms
    [checkValid2] Count: 1000000, Time: 867.07715 ms
    */
    @Test
    public void checkValidTest2() {
        int base = 0, refactor = 1;
        while (base < refactor) {
            ipValidTest2((long) Math.pow(10, base));
            base += 2;
        }
    }

    private void ipValidTest2(long len) {
        long begin, end, count, total;
        boolean actual, actual2, actual3;
        count = total = 0;
        while (count < len) {
            begin = System.nanoTime();
            actual = checkValid2("1.1.1.1");
            actual2 = checkValid2("113.12.83.4");
            actual3 = checkValid2("313.12.83.4");
            end = System.nanoTime();
            assertTrue(actual);
            assertTrue(actual2);
            assertFalse(actual3);
            total += (end - begin);
            count++;
        }
        _log.info("[checkValid2] Count: {}, Time: {} ms", count, total * Math.pow(10, -6));
    }

    @Test
    public void checkValidTestV6() {
        assertTrue(checkValidV6("2001:0db8:0000:0000:0000:ff00:0042:8329"));
        assertTrue(checkValidV6("2001:db8:0:0:0:ff00:42:8329"));
        assertTrue(checkValidV6("0000:0000:0000:0000:0000:0000:0000:0001"));
        assertTrue(checkValidV6("::1"));
        assertFalse(checkValidV6("2001:0db8:0000:0000:0000:ff00:0042:83299"));
        assertFalse(checkValidV6("::11111"));
    }

    @Test
    public void removeTail32Test() {
        assertEquals("1.1.1.1", removeTail32("1.1.1.1/32"));
        assertEquals("113.12.83.4", removeTail32("113.12.83.4/32"));
    }

    @Test
    public void extractDomainTest() {
        assertEquals("yuzhouwan.com", extractDomain("https://yuzhouwan.com"));
        assertEquals("yuzhouwan.com", extractDomain("https://yuzhouwan.com/"));
        assertEquals("yuzhouwan.com", extractDomain("https://yuzhouwan.com/subpath/welcome.html"));
        assertNull(extractDomain("http:/yuzhouwan.com"));

        assertNull(extractDomain(""));
        assertNull(extractDomain(null));
    }

    @Test
    public void getTailFromURLTest() {
        assertEquals("group1/M00/00/00/oYYBAFd06DSAQ5gwAAIIUQRZ1_c574.pdf",
                getTailFromURL("http://192.168.112.171:9090/group1/M00/00/00/oYYBAFd06DSAQ5gwAAIIUQRZ1_c574.pdf"));
    }

    @Test
    public void testLongAndIp() {
        String ipAddressStr = "192.168.5.11";
        Long ipAddressLong = 3232236811L;
        assertEquals(ipAddressStr, long2ip(ip2long(ipAddressStr)));
        if (ipAddressLong == ip2long(long2ip(ipAddressLong))) {
            System.out.println(long2ip(ipAddressLong));
            System.out.println(true);
        }
    }

    @Test
    public void checkIP2IntTest() {
        Integer ip2int = ip2int("10.1.1.0");
        Assert.notNull(ip2int);
        int ip = ip2int;
        assertEquals(167837952, ip);
        assertEquals(0x0A010100, ip);
    }

    @Test
    public void checkIPRangeTest() {
        assertNull(checkIPRange("", ""));
        assertNull(checkIPRange("", "10.1.1.0/24"));
        assertNull(checkIPRange("10.1.1.1", "10.1.1.0/"));
        assertNull(checkIPRange("10.1.1.1", "/24"));

        assertNull(checkIPRange("9999.1.1.1", "10.1.1.0/24"));

        assertEquals(true, checkIPRange("10.1.1.1", "10.1.1.0/24"));
        assertEquals(false, checkIPRange("10.1.2.1", "10.1.1.0/24"));

        assertEquals(false, checkIPRange("113.12.83.3", "113.12.83.4/32"));
        assertEquals(true, checkIPRange("113.12.83.4", "113.12.83.4/32"));

        String aim = "113.12.83.4";
        String ranges = "113.12.83.4/32,113.12.83.5/32,116.1.239.35/32";
        boolean result = false;
        if (ranges.contains(",")) {
            for (String range : ranges.split(",")) {
                Boolean isInRange = checkIPRange(aim, range);
                if (isInRange != null && isInRange) {
                    result = true;
                    break;
                }
            }
        }
        assertTrue(result);
    }

    @Disabled
    @Test
    public void getIPFromURLTest() {
        assertNull(getIPFromURL("yuzhouwan.com"));
        assertNull(getIPFromURL("www.yuzhouwan.com"));
        System.out.println(getIPFromURL("http://yuzhouwan.com/"));
        System.out.println(getIPFromURL("https://yuzhouwan.com/"));

        System.out.println(getIPFromURL("http://180.97.33.107"));
        System.out.println(getIPFromURL("ftp://11.22.33.44"));
        System.out.println(getIPFromURL("http://www.yuzhouwan.com"));
    }

    @Disabled
    @Test
    public void isReachableTest() {
        assertEquals(true, isReachable("127.0.0.1"));
        //stop following test for performance
        System.out.println(isReachable("192.168.1.101"));
        System.out.println(isReachable(getIPFromURL("https://yuzhouwan.com")));
    }

    @Disabled
    @Test
    public void pingTest() {
        System.out.println(ping("127.0.0.1"));
    }

    @Disabled
    @Test
    public void getCurrentEnvironmentNetworkIpTest() {
        List<String> ips = getCurrentEnvironmentNetworkIps();
        for (String ip : ips) {
            System.out.println(ip);
            System.out.println(isReachable(ip));
            System.out.println(ping(ip));
        }
    }
}
