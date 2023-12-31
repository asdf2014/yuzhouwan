package com.yuzhouwan.hacker.joda;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Joda Time Have a Try
 *
 * @author Benedict Jin
 * @since 2015/8/20
 */
public class JodaTimeHaveATry {

    private static Date date;

    @Before
    public void before() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        try {
            date = sdf.parse("2015-08-12-11:20:38");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() {

        DateTime jodaTime = new DateTime(date);

        assertEquals(2015, jodaTime.getYear());
        assertEquals(20, jodaTime.getMinuteOfHour());

        assertEquals("八月", jodaTime.monthOfYear().getAsShortText());
    }

    /**
     * 当前时间减少 月 or 天 ...
     */
    @Test
    public void minusTest() {

        for (int i = 0; i < 5; i++) {
            System.out.println(DateTime.now().minusMonths(i).toString("yyyyMMdd HH:mm:ss"));
            System.out.println(DateTime.now().minusDays(i).toString("yyyyMMdd HH:mm:ss"));
            System.out.println("-----------------------------");
        }
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void longTest() {
        assertEquals("Sun May 28 11:15:14 CST 2017", new Date(Long.valueOf("1495941314418")).toString());
        assertEquals("2017-05-28T11:15:14.418+08:00", new DateTime(Long.valueOf("1495941314418")).toString());
        DateTime afterOneMinter = new DateTime(Long.valueOf("1495941314418")).plusMillis(Integer.valueOf(60000L + ""));
        assertEquals("2017-05-28T11:16:14.418+08:00",
                afterOneMinter.toString());
        assertTrue(new DateTime(Long.valueOf("1496384856085")).plusMillis(Integer.valueOf(60000L + ""))
                .isAfter(new DateTime(Long.valueOf("1496384820000"))));
    }
}
