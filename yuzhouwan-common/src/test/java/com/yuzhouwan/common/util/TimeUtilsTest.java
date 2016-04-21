package com.yuzhouwan.common.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
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
    public void test() {

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        String month = simpleDateFormat.format(now);
        int m = Integer.parseInt(month);
        _log.debug("month:{}", m);
        assertEquals(m, TimeUtils.month());
    }

    @Test
    public void test2() {

        LinkedList<Integer> l = new LinkedList<Integer>();
        l.add(1);
        l.add(2);
        int counter = 0;
        for (Integer integer : l) {
            counter++;
        }
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
}