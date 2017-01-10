package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.yuzhouwan.common.util.CollectionUtils.remove4List;

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
    private static final ConcurrentHashMap<String, Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();

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
        Field[] fields = getFields(clazz, className);
        if (fields == null || fields.length == 0) return;
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
     * The method is suit for small head and long tail.
     *
     * @param objList
     * @param fields
     * @param ignores
     * @param <T>
     * @return
     */
    public static <T> LinkedList<String> columns2Row(List<T> objList, String[] fields, boolean isLongTail, Object... ignores) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, fields, isLongTail, ignores));
        return rows;
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for small head and long tail.
     *
     * @param obj
     * @param fields
     * @param ignores
     * @param <T>
     * @return
     */
    public static <T> LinkedList<String> column2Row(T obj, String[] fields, boolean isLongTail, Object... ignores) {
        if (obj == null || fields == null || fields.length == 0) return null;
        Class<?> clazz;
        if ((clazz = obj.getClass()) == null) return null;
        String className;
        if (StrUtils.isEmpty(className = clazz.getName())) return null;
        LinkedList<Field> head = new LinkedList<>(), tail = new LinkedList<>();
        boolean flag;
        for (Field cacheF : getFields(clazz, className))
            for (String aimF : fields) {
                if (StrUtils.isEmpty(aimF) || cacheF == null) continue;
                flag = aimF.equals(cacheF.getName());
                if (!isLongTail) flag = !flag;
                if (flag) head.add(cacheF);
                else tail.add(cacheF);
            }
        if (ignores != null && ignores.length > 0) {
            remove4List(head, "name", ignores);
            remove4List(tail, "name", ignores);
        }
        return column2Row(obj, head, tail);
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for small head/tail.
     *
     * @param objList
     * @param head
     * @param tail
     * @param <T>
     * @return
     */
    public static <T> LinkedList<String> column2Row(List<T> objList, LinkedList<Field> head, LinkedList<Field> tail) {
        LinkedList<String> rows = new LinkedList<>();
        for (Object o : objList) rows.addAll(column2Row(o, head, tail));
        return rows;
    }

    /**
     * Object{Str a, Str b, int c, int d} -> ["{a, b, c}", "{a, b, d}"]
     * <p>
     * The method is suit for small head/tail.
     *
     * @param obj
     * @param head
     * @param tail
     * @param <T>
     * @return
     */
    public static <T> LinkedList<String> column2Row(T obj, LinkedList<Field> head, LinkedList<Field> tail) {
        LinkedList<String> rows = new LinkedList<>();
        JSONObject jsonObject = new JSONObject();
        for (Field h : head)
            try {
                h.setAccessible(true);
                jsonObject.put(h.getName(), h.get(obj));
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        Field tailTmp;
        for (int i = 0; i < tail.size(); i++)
            try {
                (tailTmp = tail.get(i)).setAccessible(true);
                if (i > 0) jsonObject.remove(tail.get(i - 1).getName());
                jsonObject.put(tailTmp.getName(), tailTmp.get(obj));
                rows.add(jsonObject.toJSONString());
            } catch (IllegalAccessException e) {
                _log.error(ExceptionUtils.errorInfo(e));
            }
        return rows;
    }

    /**
     * Get fields with <code>FIELDS_CACHE</code>
     *
     * @param clazz
     * @param className
     * @return
     */
    public static Field[] getFields(Class<?> clazz, String className) {
        Field[] fields;
        if (FIELDS_CACHE.size() == 0 || !FIELDS_CACHE.containsKey(className))
            FIELDS_CACHE.put(className, fields = clazz.getDeclaredFields());
        else fields = FIELDS_CACHE.get(className);
        return fields;
    }
}