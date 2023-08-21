package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public final class BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);
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
        LOGGER.debug("[Warn] Cannot be swapped! object: {}, key: {}, value: {}, ignores: {}",
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
                LOGGER.error(ExceptionUtils.errorInfo(e));
                return false;
            }
            return true;
        }
        return false;
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
    public static Vector<Field> getAllFields(Class<?> clazz) {
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
    private static Vector<Field> getAllFieldsRec(Class<?> clazz, Vector<Field> v) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) getAllFieldsRec(superClass, v);
        Collections.addAll(v, clazz.getDeclaredFields());
        return v;
    }
}
