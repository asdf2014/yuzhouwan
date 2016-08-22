package com.yuzhouwan.common.util;

import java.util.*;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Collection Utils
 *
 * @author Benedict Jin
 * @since 2016/6/12
 */
public class CollectionUtils {

    /**
     * 按照 strWithSeparator 中包含的几个单词，模糊匹配 originList 内元素，并移除
     *
     * @param originList
     * @param strWithSeparator
     * @param separator
     * @return
     */
    public static List<String> removeAllByStrWithSeparator(List<String> originList, String strWithSeparator, String separator) {
        if (originList == null || originList.size() <= 0 || strWithSeparator == null || separator == null) {
            return null;
        }
        List<String> result = new LinkedList<>();
        boolean isContains;
        for (String origin : originList) {
            isContains = false;
            for (String aim : strWithSeparator.split(separator)) {
                if (!isContains && origin.contains(aim)) {
                    isContains = true;
                }
            }
            if (!isContains) {
                result.add(origin);
            }
        }
        return result;
    }

    /**
     * Remove duplicate
     *
     * @param a
     * @param b
     * @return
     */
    public static Object[] duplicate(Object[] a, Object[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) return null;

        Set<Object> set = new HashSet<>();
        Collections.addAll(set, a);

        Set<Object> result = new HashSet<>();
        for (Object bi : b) {
            if (set.contains(bi))
                result.add(bi);
        }
        return result.toArray();
    }

}
