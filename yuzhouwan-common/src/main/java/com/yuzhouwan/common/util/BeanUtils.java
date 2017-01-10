package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.yuzhouwan.common.util.CollectionUtils.remove;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public class BeanUtils {

    private static final Logger _log = LoggerFactory.getLogger(BeanUtils.class);
    private static final ConcurrentHashMap<String, Vector<Field>> FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * Swap values into object's similar filed.
     *
     * @param o       object
     * @param key     filed
     * @param value   value
     * @param ignores character to be ignored
     */
    public static void swapper(Object o, String key, Object value, String... ignores) {
        if (o == null || StrUtils.isEmpty(key)) return;
        Class<?> clazz;
        if ((clazz = o.getClass()) == null) return;
        String className;
        if (StrUtils.isEmpty(className = clazz.getName())) return;
        Vector<Field> fields = getFields(clazz, className);
        if (fields == null || fields.size() == 0) return;
        for (Field field : fields) {
            if (field == null) continue;
            if (StrUtils.isLike(field.getName(), key, ignores)) {
                field.setAccessible(true);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    _log.error(ExceptionUtils.errorInfo(e));
                }
                return;
            }
        }
        _log.debug("[Warn] Cannot be swapped, object: {}, key: {}, value: {}, ignores: {}.",
                o, key, value, JSON.toJSONString(ignores));
    }

    /**
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     * <p>
     * The method is suit for (small head && long tail / large head && short tail).
     *
     * @param objList    aim object list
     * @param fields     fields
     * @param isLongTail true:  $fields is head
     *                   false: $fields is tail
     * @param ignores    ignored fields
     * @param <T>        generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, String[] fields, boolean isLongTail, Object... ignores) {
        return columns2Row(objList, fields, null, isLongTail, ignores);
    }

    /**
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     * <p>
     * The method is suit for (small head && long tail / large head && short tail).
     *
     * @param objList     aim object list
     * @param fields      fields
     * @param parentClass get some fields from parent class
     * @param isLongTail  true:  $fields is head
     *                    false: $fields is tail
     * @param ignores     ignored fields
     * @param <T>         generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, String[] fields, Class parentClass, boolean isLongTail, Object... ignores) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, fields, parentClass, isLongTail, ignores));
        return rows;
    }

    /**
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     * <p>
     * The method is suit for (small head && long tail / large head && short tail).
     *
     * @param obj        aim object
     * @param fields     fields
     * @param isLongTail true:  $fields is head
     *                   false: $fields is tail
     * @param ignores    ignored fields
     * @param <T>        generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, String[] fields, boolean isLongTail, Object... ignores) {
        return column2Row(obj, fields, null, isLongTail, ignores);
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for (small head && long tail / large head && short tail).
     *
     * @param obj         aim object
     * @param fields      fields
     * @param parentClass get some fields from parent class
     * @param isLongTail  true:  $fields is head
     *                    false: $fields is tail
     * @param ignores     ignored fields
     * @param <T>         generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, String[] fields, Class parentClass, boolean isLongTail, Object... ignores) {
        if (obj == null || fields == null || fields.length == 0) return null;
        Class<?> clazz;
        if ((clazz = obj.getClass()) == null) return null;
        String className;
        if (StrUtils.isEmpty(className = clazz.getName())) return null;
        Set<Field> head = new HashSet<>(), tail = new HashSet<>();
        for (Field field : getFields(clazz, className)) {
            head.add(field);
            tail.add(field);
        }
        if (parentClass != null)
            for (Field field : parentClass.getDeclaredFields()) {
                head.add(field);
                tail.add(field);
            }
        if (isLongTail) {
            remove(tail, "name", (Object[]) fields);
            Collection<Field> cHead = remove(head, "name", (Object[]) fields);
            head.clear();
            head.addAll(cHead);
        } else {
            remove(head, "name", (Object[]) fields);
            Collection<Field> cTail = remove(tail, "name", (Object[]) fields);
            tail.clear();
            tail.addAll(cTail);
        }
        if (ignores != null && ignores.length > 0) {
            remove(head, "name", ignores);
            remove(tail, "name", ignores);
        }
        return column2Row(obj, head, tail);
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for small head/tail.
     *
     * @param objList aim object list
     * @param head    head fields
     * @param tail    tail fields
     * @param <T>     generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, Set<Field> head, Set<Field> tail) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, head, tail));
        return rows;
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for small head/tail.
     *
     * @param obj  aim object
     * @param head head fields
     * @param tail tail fields
     * @param <T>  generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, Set<Field> head, Set<Field> tail) {
        LinkedList<String> rows = new LinkedList<>();
        JSONObject jsonObject = new JSONObject();
        for (Field h : head)
            try {
                h.setAccessible(true);
                jsonObject.put(h.getName(), h.get(obj));
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        Field tailPrev = null;
        for (Field t : tail)
            try {
                t.setAccessible(true);
                if (tailPrev != null) jsonObject.remove(tailPrev.getName());
                jsonObject.put(t.getName(), t.get(obj));
                rows.add(jsonObject.toJSONString());
                tailPrev = t;
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        return rows;
    }

    /**
     * Get fields with <code>FIELDS_CACHE</code>
     *
     * @param clazz     class
     * @param className the simple name of class as the key in FIELDS_CACHE
     * @return fields
     */
    public static Vector<Field> getFields(Class<?> clazz, String className) {
        Vector<Field> fields;
        if (FIELDS_CACHE.size() == 0 || !FIELDS_CACHE.containsKey(className))
            FIELDS_CACHE.put(className, fields = getAllFields(clazz));
        else fields = FIELDS_CACHE.get(className);
        return fields;
    }

    /**
     * Return the set of fields declared at all level of class hierarchy
     *
     * @param clazz class
     * @return all fields in class and it's super classes
     */
    public static Vector<Field> getAllFields(Class clazz) {
        return getAllFieldsRec(clazz, new Vector<>()).stream().filter(field -> !field.getName().equals("this$0"))
                .collect(Collectors.toCollection(Vector::new));
    }

    /**
     * Get all fields from super classes with recursion.
     *
     * @param clazz  class
     * @param vector hold fields
     * @return fields
     */
    private static Vector<Field> getAllFieldsRec(Class clazz, Vector<Field> vector) {
        Class superClazz;
        if ((superClazz = clazz.getSuperclass()) != null) getAllFieldsRec(superClazz, vector);
        Collections.addAll(vector, clazz.getDeclaredFields());
        return vector;
    }
}