package com.yuzhouwan.hacker.joda;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Benedict Jin on 2015/8/20.
 */
public class JodaTimeHaveATry {

    private static Date date;

    @Before
    public void before(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");

        try {
            date = sdf.parse("2015-08-12-11:20:38");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test() {

        DateTime jodaTime = new DateTime(date);

        assertEquals(2015, jodaTime.getYear());
        assertEquals(20, jodaTime.getMinuteOfHour());

        assertEquals("八月", jodaTime.monthOfYear().getAsShortText());

    }


}
