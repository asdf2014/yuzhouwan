package com.yuzhouwan.util;

import java.util.Calendar;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Time Util
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class TimeUtils {

    public static int month() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

}
