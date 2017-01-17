package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Collection Utils
 *
 * @author Benedict Jin
 * @since 2016/6/12
 */
public class CollectionUtils {

    private static final Logger _log = LoggerFactory.getLogger(CollectionUtils.class);

//    private volatile static long NANO_SECOND_TOTAL;
//    private volatile static long CALL_COUNT_TOTAL;

    /**
     * 按照 strWithSeparator 中包含的几个单词，模糊匹配 originList 内元素，并移除
     *
     * @param originList
     * @param strWithSeparator
     * @param separator
     * @return
     */
    public static List<String> removeAllByStrWithSeparator(List<String> originList, String strWithSeparator, String separator) {
        if (originList == null || originList.size() <= 0 || strWithSeparator == null || separator == null) return null;
        List<String> result = new LinkedList<>();
        boolean isContains;
        for (String origin : originList) {
            isContains = false;
            for (String aim : strWithSeparator.split(separator))
                if (!isContains && origin.contains(aim)) isContains = true;
            if (!isContains) result.add(origin);
        }
        return result;
    }

    /**
     * Remove Duplicate for Object[]
     *
     * @param a
     * @param b
     * @return
     */
    public static Object[] intersection(Object[] a, Object[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) return null;
        LinkedHashSet<Object> set = new LinkedHashSet<>(), result = new LinkedHashSet<>();
        Collections.addAll(set, a);
        for (Object bi : b) if (set.contains(bi)) result.add(bi);
        return result.toArray();
    }

    /**
     * Remove Duplicate for Collection
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> Collection<T> intersection(Collection<T> a, Collection<T> b) {
        if (a == null || b == null || a.size() == 0 || b.size() == 0) return null;
        LinkedHashSet<T> set = new LinkedHashSet<>();
        set.addAll(a);
        return b.stream().filter(set::contains).collect(Collectors.toSet());
    }

    public static <E> Object getDuplicate(Collection<E> coll, E o, String field, Class fieldClass) {
        return getDuplicate(coll, o, field, fieldClass, null);
    }

    /**
     * Get Duplicate from Collection
     * [Note]: Should use HashMap when the coll holds a lot of data.
     *
     * @param coll         collection
     * @param o            aim object
     * @param fieldName    which fieldName is used for judging
     * @param fieldClass   the class type of fieldName
     * @param elementClass the sub-class of element in collection
     * @param <E>          the class type of elements in collection
     * @return the object which has same the value of fieldName in collection
     */
    public static <E> Object getDuplicate(Collection<E> coll, E o, String fieldName, Class fieldClass, Class elementClass) {

//        long startTime = System.nanoTime();
        if (coll == null || coll.isEmpty() || o == null || StrUtils.isEmpty(fieldName) || fieldClass == null)
            return null;
        Object collO = null, aimO = null;
        String elementClassName = "";
        boolean subClass = false;
        if (elementClass != null) {
            elementClassName = elementClass.getName();
            subClass = StrUtils.isNotEmpty(elementClassName);
        }
        E end = null;
        try {
            Field f = o.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
//            long endTime, period;
            for (E e : coll) {
                if (subClass && !elementClassName.equals(e.getClass().getName())) continue;
                collO = f.get(e);
                aimO = f.get(o);
                if (collO.equals(aimO) || fieldClass.cast(collO).equals(fieldClass.cast(aimO))) {
//                    endTime = System.nanoTime();
//                    period = endTime - startTime;
                    // 2016-12-08 09:07:56.746 | DEBUG | getDuplicate method used time: 622 nanosecond, total count: 57746148, total time: 1057645752619 nanosecond, time pre call: 18315 nanosecond | com.yuzhouwan.common.collection.CollectionUtils.getDuplicate | CollectionUtils.java:104
//                    _log.debug("getDuplicate method used time: {} nanosecond, total count: {}, " +
//                                    "total time: {} nanosecond, time pre call: {} nanosecond",
//                            period, CALL_COUNT_TOTAL++, (NANO_SECOND_TOTAL = NANO_SECOND_TOTAL + period),
//                            NANO_SECOND_TOTAL / CALL_COUNT_TOTAL);
                    return (end = e);
                }
            }
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e,
                    String.format("fieldName: %s, class: %s, object in collection: %s, object aim: %s",
                            fieldName, fieldClass.getName(), collO, aimO)));
        } finally {
            if (end != null) coll.remove(end);
        }
        return null;
    }

    /**
     * Remove element from List with special field.
     *
     * @param coll    aim collection
     * @param field   could be null, meaning those elements in coll have a simple class type
     * @param removes need be removed elements or those fields' value of elements
     * @param <T>     generic type
     */
    public static <T> Collection<T> remove(Collection<T> coll, final String field, final Object... removes) {
        if (coll == null || coll.size() == 0 || removes == null || removes.length == 0) return null;
        Field f;
        Object tmp;
        Collection<T> needRemoved = new LinkedList<>();
        try {
            for (T c : coll)
                for (Object remove : removes) {
                    if (StrUtils.isNotEmpty(field)) {
                        f = c.getClass().getDeclaredField(field);
                        f.setAccessible(true);
                        tmp = f.get(c);
                    } else tmp = c;
                    if (tmp.equals(remove)) needRemoved.add(c);
                }
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        coll.removeAll(needRemoved);
        return needRemoved;
    }
}