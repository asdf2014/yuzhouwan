package com.yuzhouwan.common.util;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: String Utils
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
     * 得到最主干的数值，处理类似：HEAD000001
     *
     * @param suppressCode
     * @param headIndex
     * @return 1
     */
    public static String getMainValue(String suppressCode, int headIndex, String needRemoved) {
        if (!StrUtils.isEmpty(suppressCode)) {
            String tail = suppressCode.substring(headIndex, suppressCode.length());
            while (tail.length() >= 0) {
                if (tail.startsWith(needRemoved)) tail = tail.substring(1);
                else return tail;

            }
        }
        return null;
    }

    /**
     * Parsing String is Empty.
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Parsing String is not Empty.
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(final String s) {
        return !isEmpty(s);
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
        String[] strs;
        return (strs = origin.split(middle))[strs.length - 1];
    }

    /**
     * Cut the tail of string.
     *
     * @param origin
     * @param tail
     * @return
     */
    public static String cutTailStr(String origin, String tail) {
        return origin.substring(0, origin.length() - tail.length());
    }

    /**
     * Split String with Multi separators.
     *
     * @param origin     origin String
     * @param separators separator array
     * @return
     */
    public static LinkedList<String> splitMulti(String origin, final String... separators) {
        int len;
        if (StrUtils.isEmpty(origin) || separators == null || (len = separators.length) == 0) return null;
        int index;
        LinkedList<String> result = new LinkedList<>();
        for (int i = 0; i < separators.length; i++) {
            if ((index = origin.indexOf(separators[i])) == -1) break;
            origin = origin.substring(index + separators[i].length());
            if (i == len - 1) {
                result.add(origin);
                break;
            }
            index = origin.indexOf(separators[i + 1]);
            if (index == -1) break;
            result.add(origin.substring(0, index));
            origin = origin.substring(index);
        }
        return result;
    }

    /**
     * Judge two string is like without ignore characters.
     *
     * @param origin  origin string
     * @param aim     aim string
     * @param ignores characters to ignores
     * @return
     */
    public static boolean isLike(String origin, String aim, final String... ignores) {
        if (StrUtils.isEmpty(origin) || StrUtils.isEmpty(aim)) return false;
        if (ignores != null && ignores.length > 0)
            for (String ignore : ignores) {
                origin = origin.replaceAll(ignore, "");
                aim = aim.replaceAll(ignore, "");
            }
        return origin.equalsIgnoreCase(aim);
    }

    /**
     * Judge str is number.
     *
     * @param s string
     * @return isNumber
     */
    public static boolean isNumber(final String s) {
        if (isEmpty(s)) return false;
        for (int i = 0; i < s.length(); i++) if (!Character.isDigit(s.charAt(i))) return false;
        return true;
    }
}
