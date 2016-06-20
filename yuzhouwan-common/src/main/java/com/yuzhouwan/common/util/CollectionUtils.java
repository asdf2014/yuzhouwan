package com.yuzhouwan.common.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Collection Util
 *
 * @author Benedict Jin
 * @since 2016/6/12 0030
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

}
