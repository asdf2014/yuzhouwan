package com.yuzhouwan.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.LinkedList;

import static java.lang.Character.isDigit;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: String Utils
 *
 * @author Benedict Jin
 * @since 2016/3/23
 */
public final class StrUtils {

    public static final String ZERO = "0";
    public static final String HEX = "\\x";

    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String DOUBLE_TRANSFER = "\\\\";

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final char POINT = '.';

    public static final String UTF_8 = "UTF-8";

    private StrUtils() {
    }

    /**
     * 用 "0" 填充 aim 数值之前的 (num-((int)aim).length) 个空位.
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
     * 得到最主干的数值，处理类似：HEAD000001.
     *
     * @param suppressCode
     * @param headIndex
     * @return 1
     */
    public static String getMainValue(String suppressCode, int headIndex, String needRemoved) {
        if (!StrUtils.isEmpty(suppressCode)) {
            String tail = suppressCode.substring(headIndex, suppressCode.length());
            while (true)
                if (tail.startsWith(needRemoved)) tail = tail.substring(1);
                else return tail;
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
        return s == null || s.isEmpty();
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
     * Parsing String is Blank.
     *
     * @param s
     * @return
     */
    public static boolean isBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }

    /**
     * Parsing String is not Blank.
     *
     * @param s
     * @return
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * Cut Start String.
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
    public static boolean isLike(final String origin, final String aim, final String... ignores) {
        if (StrUtils.isEmpty(origin) || StrUtils.isEmpty(aim)) return false;
        String originCopy = origin.intern();
        String aimCopy = aim.intern();
        if (ignores != null && ignores.length > 0)
            for (String ignore : ignores) {
                originCopy = originCopy.replaceAll(ignore, "");
                aimCopy = aimCopy.replaceAll(ignore, "");
            }
        return originCopy.equalsIgnoreCase(aimCopy);
    }

    /**
     * Judge str is number.
     *
     * @param s string
     * @return isNumber
     */
    public static boolean isNumber(final String s) {
        if (isBlank(s)) return false;
        char c;
        boolean pointExist = false;
        int start = s.startsWith(PLUS) || s.startsWith(MINUS) ? 1 : 0;
        int len = s.length();
        int len_minus_one = len - 1;
        if (start == len) return false;
        for (int i = start; i < len; i++) {
            c = s.charAt(i);
            if (c == POINT) {
                if (i == start || i == len_minus_one || pointExist) return false;
                else {
                    pointExist = true;
                    continue;
                }
            }
            if (!isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Convert String form UTF-8 into Hex.
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String str2Hex(String s) throws UnsupportedEncodingException {
        if (StrUtils.isEmpty(s)) return s;
        char[] hexRawArr = String.format("%x", new BigInteger(1, s.getBytes(UTF_8))).toCharArray();
        StringBuilder hexFmtStr = new StringBuilder();
        for (int i = 0; i < hexRawArr.length; i++) hexFmtStr.append(HEX).append(hexRawArr[i]).append(hexRawArr[++i]);
        return hexFmtStr.toString();
    }

    /**
     * Convert String form Hex into UTF-8.
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String hex2Str(String s) throws UnsupportedEncodingException {
        if (StrUtils.isEmpty(s)) return s;
        String[] strArr = s.split(DOUBLE_TRANSFER);
        byte[] byteArr = new byte[strArr.length - 1];
        for (int i = 1; i < strArr.length; i++) byteArr[i - 1] = Integer.decode(ZERO.concat(strArr[i])).byteValue();
        return new String(byteArr, UTF_8);
    }

    /**
     * Compression String.
     *
     * @param s
     * @return
     */
    public static String compression(String s) {
        if (isEmpty(s)) return null;
        return s.replaceAll(" ", "").replaceAll("\\r\\n", "").replaceAll("\\r", "").replaceAll("\\n", "");
    }
}
