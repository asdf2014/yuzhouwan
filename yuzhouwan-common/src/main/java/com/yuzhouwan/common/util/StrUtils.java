package com.yuzhouwan.common.util;

import java.text.DecimalFormat;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: String Stuff
 *
 * @author Benedict Jin
 * @since 2016/3/23 0030
 */
public class StrUtils {

    public static final String ZERO = "0";

    /**
     * 用 "0" 填充 aim数值之前的 (num-((int)aim).length)个空位
     *
     * @param aim
     * @param num
     * @return
     */
    public static String fillWitchZero(Number aim, int num) {
        String zeros = "";
        if (num > 0) {
            int count = 0;
            while (count < num) {
                zeros = zeros.concat(ZERO);
                count++;
            }
            return new DecimalFormat(zeros).format(aim);
        }
        return aim.toString();
    }

    /**
     * Parsing String is Empty.
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Cut Start String
     *
     * @param origin
     * @param start
     * @return
     */
    public static String cutStartStr(String origin, String start) {
        return origin.substring(start.length(), origin.length());
    }

    /**
     * Cut Middle String, and Save the tail.
     *
     * @param origin
     * @param middle
     * @return
     */
    public static String cutMiddleStr(String origin, String middle) {
        String[] strs = origin.split(middle);
        return strs[strs.length - 1];
    }

    public static String cutTailStr(String origin, String tail) {
        return origin.substring(0, origin.length() - tail.length());
    }
}
