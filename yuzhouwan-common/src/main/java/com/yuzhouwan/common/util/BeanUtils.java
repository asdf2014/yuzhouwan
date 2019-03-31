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
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public final class BeanUtils {

    private static final Logger _log = LoggerFactory.getLogger(BeanUtils.class);
    private static final String FIELD_NAME = "name", DRUID_METRICS = "metric", DRUID_VALUE = "value";
    private static final ConcurrentHashMap<String, Vector<Field>> FIELDS_CACHE = new ConcurrentHashMap<>();

    private BeanUtils() {
    }

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
        Class<?> clazz = o.getClass();
        if (clazz == null) return;
        String className = clazz.getName();
        if (StrUtils.isEmpty(className)) return;
        Vector<Field> fields = getFields(clazz, className);
        if (fields == null || fields.size() == 0) return;
        for (Field field : fields) {
            if (field == null) continue;
            if (valueField(o, key, value, field, ignores)) return;
        }
        _log.debug("[Warn] Cannot be swapped! object: {}, key: {}, value: {}, ignores: {}",
                o, key, value, JSON.toJSONString(ignores));
    }

    /**
     * Value field.
     *
     * @param o
     * @param key
     * @param value
     * @param field
     * @param ignores
     * @return
     */
    private static boolean valueField(Object o, String key, Object value, Field field, String[] ignores) {
        if (StrUtils.isLike(field.getName(), key, ignores)) {
            field.setAccessible(true);
            try {
                field.set(o, value);
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * The method is suit for (small head && long tail / large head && short tail).
     * <p>
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     *
     * @param objList    aim object list
     * @param fields     fields
     * @param isLongTail true:  $fields is head
     *                   false: $fields is tail
     * @param forDruid   true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                   false: normal {..., "xxx": yyy}
     * @param ignores    ignored fields
     * @param <T>        generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, String[] fields,
                                                     boolean isLongTail, boolean forDruid, Object... ignores) {
        return columns2Row(objList, fields, null, isLongTail, forDruid, ignores);
    }

    /**
     * The method is suit for (small head && long tail / large head && short tail).
     * <p>
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     *
     * @param objList     aim object list
     * @param fields      fields
     * @param parentClass get some fields from parent class
     * @param isLongTail  true:  $fields is head
     *                    false: $fields is tail
     * @param forDruid    true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                    false: normal {..., "xxx": yyy}
     * @param ignores     ignored fields
     * @param <T>         generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, String[] fields,
                                                     Class parentClass, boolean isLongTail,
                                                     boolean forDruid, Object... ignores) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, fields, parentClass, isLongTail, forDruid, ignores));
        return rows;
    }

    /**
     * The method is suit for (small head && long tail / large head && short tail).
     * <p>
     * [Object{Str a, Str b, int c, int d}, ...] -> ["{a, b, c}", "{a, b, d}", ...]
     *
     * @param obj        aim object
     * @param fields     fields
     * @param isLongTail true:  $fields is head
     *                   false: $fields is tail
     * @param forDruid   true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                   false: normal {..., "xxx": yyy}
     * @param ignores    ignored fields
     * @param <T>        generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, String[] fields, boolean isLongTail,
                                                    boolean forDruid, Object... ignores) {
        return column2Row(obj, fields, null, isLongTail, forDruid, ignores);
    }

    /**
     * The method is suit for (small head && long tail / large head && short tail).
     * <p>
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     *
     * @param obj         aim object
     * @param fields      fields
     * @param parentClass get some fields from parent class
     * @param isLongTail  true:  $fields is head
     *                    false: $fields is tail
     * @param forDruid    true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                    false: normal {..., "xxx": yyy}
     * @param ignores     ignored fields
     * @param <T>         generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, String[] fields, Class parentClass,
                                                    boolean isLongTail, boolean forDruid, Object... ignores) {
        if (obj == null || fields == null || fields.length == 0) return null;
        Class<?> clazz = obj.getClass();
        if (clazz == null) return null;
        String className = clazz.getName();
        if (StrUtils.isEmpty(className)) return null;
        Set<Field> head = new HashSet<>(), tail = new HashSet<>();
        initFields(head, tail, clazz, className, parentClass, isLongTail);
        removeFields(head, tail, isLongTail, fields, ignores);
        return column2Row(obj, head, tail, forDruid);
    }

    /**
     * Init total fields.
     *
     * @param head
     * @param tail
     * @param clazz
     * @param className
     * @param parentClass
     * @param isLongTail
     */
    private static void initFields(Set<Field> head, Set<Field> tail, Class<?> clazz,
                                   String className, Class parentClass, boolean isLongTail) {
        Vector<Field> fieldVector = getFields(clazz, className);
        if (isLongTail) tail.addAll(fieldVector);
        else head.addAll(fieldVector);
        if (parentClass != null) Collections.addAll(isLongTail ? tail : head, parentClass.getDeclaredFields());
    }

    /**
     * Remove fields.
     * [note]: 1. tail/head: difference set; 2. removed: intersection in remove method.
     *
     * @param head
     * @param tail
     * @param isLongTail
     * @param fields
     * @param ignores
     */
    private static void removeFields(Set<Field> head, Set<Field> tail, boolean isLongTail,
                                     Object[] fields, Object[] ignores) {
        if (isLongTail) head.addAll(remove(tail, FIELD_NAME, fields));
        else tail.addAll(remove(head, FIELD_NAME, fields));
        if (ignores != null && ignores.length > 0) {
            remove(head, FIELD_NAME, ignores);
            remove(tail, FIELD_NAME, ignores);
        }
    }

    /**
     * The method is suit for small head/tail.
     * <p>
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     *
     * @param objList  aim object list
     * @param head     head fields
     * @param tail     tail fields
     * @param forDruid true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                 false: normal {..., "xxx": yyy}
     * @param <T>      generic type
     * @return rows
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, Set<Field> head,
                                                     Set<Field> tail, boolean forDruid) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, head, tail, forDruid));
        return rows;
    }

    /**
     * The method is suit for small head/tail.
     * <p>
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     *
     * @param obj      aim object
     * @param head     head fields
     * @param tail     tail fields
     * @param forDruid true:  tail will be format as {..., "metric": "xxx", "value": yyy}
     *                 false: normal {..., "xxx": yyy}
     * @param <T>      generic type
     * @return rows
     */
    public static <T> LinkedList<String> column2Row(T obj, Set<Field> head, Set<Field> tail, boolean forDruid) {
        JSONObject jsonObject = new JSONObject();
        for (Field f : head)
            try {
                f.setAccessible(true);
                jsonObject.put(f.getName(), f.get(obj));
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        LinkedList<String> rows = new LinkedList<>();
        if (forDruid) dealWithTail4Druid(obj, tail, rows, jsonObject);
        else dealWithTail(obj, tail, rows, jsonObject);
        return rows;
    }

    /**
     * Deal with tail that will be format as {..., "xxx": yyy} normally.
     *
     * @param obj
     * @param tail
     * @param rows
     * @param jsonObject
     * @param <T>
     */
    public static <T> void dealWithTail(T obj, Set<Field> tail, LinkedList<String> rows, JSONObject jsonObject) {
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
    }

    /**
     * Deal with tail that will be format as {..., "metric": "xxx", "value": yyy}.
     *
     * @param obj
     * @param tail
     * @param rows
     * @param jsonObject
     * @param <T>
     */
    public static <T> void dealWithTail4Druid(T obj, Set<Field> tail, LinkedList<String> rows, JSONObject jsonObject) {
        boolean isFirst = true;
        for (Field t : tail)
            try {
                t.setAccessible(true);
                if (!isFirst) {
                    jsonObject.remove(DRUID_METRICS);
                    jsonObject.remove(DRUID_VALUE);
                }
                if (isFirst) isFirst = false;
                jsonObject.put(DRUID_METRICS, t.getName());
//                jsonObject.put("value_".concat(t.getType().getSimpleName()), t.get(obj));
                // then should use double sum/max/min with Aggregation in Druid
                jsonObject.put(DRUID_VALUE, t.get(obj));
                rows.add(jsonObject.toJSONString());
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
    }

    /**
     * Get fields with <code>FIELDS_CACHE</code>.
     *
     * @param clazz     class
     * @param className the name of class as the key in FIELDS_CACHE
     * @return fields
     */
    public static Vector<Field> getFields(Class<?> clazz, String className) {
        if (FIELDS_CACHE.size() == 0 || !FIELDS_CACHE.containsKey(className)) {
            Vector<Field> fields = getAllFields(clazz);
            FIELDS_CACHE.put(className, fields);
            return fields;
        } else return FIELDS_CACHE.get(className);
    }

    /**
     * Return the set of fields declared at all level of class hierarchy.
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
     * @param clazz class
     * @param v     hold fields
     * @return fields
     */
    private static Vector<Field> getAllFieldsRec(Class clazz, Vector<Field> v) {
        Class superClass = clazz.getSuperclass();
        if (superClass != null) getAllFieldsRec(superClass, v);
        Collections.addAll(v, clazz.getDeclaredFields());
        return v;
    }
}
