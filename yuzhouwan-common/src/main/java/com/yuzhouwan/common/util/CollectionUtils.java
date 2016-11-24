package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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

    private static Logger _log = LoggerFactory.getLogger(CollectionUtils.class);

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

    /**
     * Get Duplicate from Collection
     *
     * @param coll
     * @param o
     * @param field
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> Object getDuplicate(Collection<E> coll, E o, String field, Class clazz) {

        if (coll == null || coll.isEmpty() || o == null || StrUtils.isEmpty(field) || clazz == null) return null;
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            Object collO, aimO;
            for (E e : coll) {
                collO = f.get(e);
                aimO = f.get(o);
                if (collO.equals(aimO) || clazz.cast(collO).equals(clazz.cast(aimO))) {
                    coll.remove(e);
                    return e;
                }
            }
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
        }
        return null;
    }
}
