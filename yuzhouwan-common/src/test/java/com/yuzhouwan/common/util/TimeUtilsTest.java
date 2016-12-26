package com.yuzhouwan.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：TimeUtils Test
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class TimeUtilsTest {

    private Logger _log = LoggerFactory.getLogger(TimeUtilsTest.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z");

    @Test
    public void nowStrTest() throws Exception {
        // 2016-11-24 09:41:52 511
        System.out.println(TimeUtils.nowStr());
    }

    @Test
    public void test() {
        int m = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        _log.debug("month:{}", m);
        assertEquals(m, TimeUtils.month());
    }

    @Test
    public void test2() {

        LinkedList<Integer> l = new LinkedList<>();
        l.add(1);
        l.add(2);
        int counter = 0;
        for (Integer integer : l) {
            _log.debug("{}", integer);
            counter++;
        }
        assertEquals(2, counter);
    }

    @Test
    public void testYesterdayBegin() {

        System.out.println(sdf.format(TimeUtils.yesterdayBegin()));
    }

    @Test
    public void testYesterdayEnd() {

        System.out.println(sdf.format(TimeUtils.yesterdayEnd()));
    }

    @Test
    public void testPatWeekStart() {
        System.out.println(sdf.format(TimeUtils.pastWeekStart()));
    }

    @Test
    public void testMoreThanTwoHour() {
        long twoHour = 1000 * 60 * 60 * 2;
        System.out.println(twoHour);
        System.out.println(TimeUtils.howLongBeginThisMonth());
    }

    @Test
    public void testBigDecimal() {
        {
            BigDecimal b = new BigDecimal(200).multiply(new BigDecimal(0.5));
            assertEquals("100.0", b.toPlainString());
        }
        {
            BigDecimal b = new BigDecimal(200).divide(new BigDecimal(2));
            assertEquals("100", b.toPlainString());
        }

        {
            BigDecimal b = new BigDecimal(333).multiply(new BigDecimal(0.5));
            assertEquals("166.5", b.toPlainString());
        }
        {
            BigDecimal b = new BigDecimal(333).divide(new BigDecimal(2));
            assertEquals("166.5", b.toPlainString());
        }
    }

    @Test
    public void testNow() {
        System.out.println(new Date().getTime());
    }

    @Test
    public void testTime() {
        {
            Calendar c = Calendar.getInstance();
            c.set(2016, Calendar.MAY, 16, 12, 0, 0);
            System.out.println(c.getTime());
            System.out.println(c.getTime().getTime());
        }
        {
            Calendar c = Calendar.getInstance();
            c.set(2016, Calendar.MAY, 16, 12, 0, 29);
            System.out.println(c.getTime());
            System.out.println(c.getTime().getTime());
        }
        {
            Calendar c = Calendar.getInstance();
            c.set(2016, Calendar.MAY, 16, 12, 0, 35);
            System.out.println(c.getTime());
            System.out.println(c.getTime().getTime());
        }
        {
            Calendar c = Calendar.getInstance();
            c.set(2016, Calendar.MAY, 16, 12, 1, 35);
            System.out.println(c.getTime());
            System.out.println(c.getTime().getTime());
        }
    }

    @Test
    public void lastMonthTodayInBeginTest() {
        System.out.println(sdf.format(TimeUtils.lastMonthTodayInBegin()));
        System.out.println(sdf.format(TimeUtils.lastFewDaysInBegin(30)));
    }

    @Test
    public void pastWeekStartTest() {
        // now: 2016-12-27 00:00:00:000 +0800
        // 2016-12-27 00:00:00:000 +0800
        // 2016-12-26 00:00:00:000 +0800
        // 2016-12-19 00:00:00:000 +0800
        // 2016-11-25 00:00:00:000 +0800
        System.out.println(sdf.format(TimeUtils.fewDaysAgoBegin(-1)));
        System.out.println(sdf.format(TimeUtils.fewDaysAgoBegin(0)));
        System.out.println(sdf.format(TimeUtils.fewDaysAgoBegin(7)));
        System.out.println(sdf.format(TimeUtils.fewDaysAgoBegin(31)));
    }

    @Test
    public void beginMonthTester() {
        assertEquals("2016-07-01 00:00:00:000 +0800", sdf.format(TimeUtils.beginMonth(2016, 7)));
    }

    @Test
    public void endMonthTester() {
        assertEquals("2016-07-31 23:59:59:999 +0800", sdf.format(TimeUtils.endMonth(2016, 7)));
    }

    @Test
    public void nowTester() {
        System.out.println(DateTime.now().toString("yyyyMM"));

        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void timeZone() throws Exception {
        Date now = new Date();
        Date past = TimeUtils.zeroTimeZone(now);
        assertEquals(0, now.getTime() - past.getTime());

        // 2016-11-25T07:01:23.000Z

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        // 2016-11-25T15:06:59.401CST
        System.out.println(sdf.format(now));

        // 2016-11-25T15:07:28.233+0800
        System.out.println(TimeUtils.nowTimeWithZone());

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        // 2016-11-25T07:13:29.000Z
        System.out.println(dtf.parseDateTime(sdf.format(now)).withZone(DateTimeZone.UTC));
    }

    @Test
    public void nanoTime() {
        long start = System.nanoTime();
        System.out.println("Nano Time:");
        long end = System.nanoTime();
        System.out.println(end - start);
    }
}