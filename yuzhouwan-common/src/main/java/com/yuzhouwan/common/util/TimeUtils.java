package com.yuzhouwan.common.util;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Time Utils
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class TimeUtils {

    /**
     * 今天是几月份
     *
     * @return
     */
    public static int month() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Date yesterdayBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date yesterdayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTime().getTime() - 1);
    }

    public static Date pastWeekStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTime().getTime());
    }

    public static Long howLongBeginThisMonth() {

        return System.currentTimeMillis() - beginThisMonth().getTime();
    }

    public static Date beginThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 上个月的同一天，凌晨 0点
     *
     * @return
     */
    public static Date lastMonthTodayInBegin() {
        return DateTime.now().minusMonths(1)
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
                .toDate();
    }

    /**
     * 几天前，凌晨 00:00:00 000
     *
     * @return
     */
    public static Date lastFewDaysInBegin(Integer days) {
        return DateTime.now().minusDays(days)
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
                .toDate();
    }

    /**
     * 某天的最后一秒 23:59:59 999
     *
     * @param index -1：今天的最后一秒；0：昨天的最后一秒；1：前天最后一秒
     * @return
     */
    public static Date yesterdayEnd(int index) {
        Calendar calendar = Calendar.getInstance();
        //设置 年月日中的“天” 为 (today - index)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - index);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //减一毫秒，变成昨天的最后一秒
        return new Date(calendar.getTime().getTime() - 1);
    }

    /**
     * 某天的开始 00:00:00 000
     *
     * @param index
     * @return
     */
    public static Date pastWeekStart(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - index);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTime().getTime());
    }

    /**
     * 某年某月 最开始一时刻
     *
     * @param year  2016
     * @param month 7 (means August, index start with zero)
     * @return
     */
    public static Date beginMonth(int year, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTime().getTime());
    }

    /**
     * 某年某月 最后一时刻
     *
     * @param year
     * @param month
     * @return
     */
    public static Date endMonth(int year, int month) {
        return new Date(new DateTime(beginMonth(year, month)).plusMonths(1).toDate().getTime() - 1);
    }
}
