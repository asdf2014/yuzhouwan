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

        if (coll == null || coll.isEmpty() || o == null || StrUtils.isEmpty(fieldName) || fieldClass == null)
            return null;
        Object collO = null, aimO = null;
        String elementClassName = "";
        boolean subClass = false;
        if (elementClass != null) subClass = StrUtils.isNotEmpty(elementClassName = elementClass.getName());
        E end = null;
        try {
            Field f = o.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            for (E e : coll) {
                if (subClass && !elementClassName.equals(e.getClass().getName())) continue;
                collO = f.get(e);
                aimO = f.get(o);
                if (collO.equals(aimO) || fieldClass.cast(collO).equals(fieldClass.cast(aimO))) return (end = e);
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
        if (coll == null || coll.size() == 0 || removes == null || removes.length == 0) return coll;
        Collection<T> needRemoved = new LinkedList<>();
        try {
            for (T c : coll) if (canRemove(field, c, removes)) needRemoved.add(c);
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        coll.removeAll(needRemoved);
        return needRemoved;
    }

    /**
     * Check the object could be removed.
     *
     * @param field
     * @param c
     * @param removes
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static <T> boolean canRemove(String field, T c, Object[] removes) throws NoSuchFieldException, IllegalAccessException {
        Field f;
        Object tmp;
        for (Object remove : removes) {
            if (StrUtils.isNotEmpty(field)) {
                f = c.getClass().getDeclaredField(field);
                f.setAccessible(true);
                tmp = f.get(c);
            } else tmp = c;
            if (tmp.equals(remove)) return true;
        }
        return false;
    }
}