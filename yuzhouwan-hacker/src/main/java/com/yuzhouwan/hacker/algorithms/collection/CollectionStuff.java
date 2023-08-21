package com.yuzhouwan.hacker.algorithms.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Collection Stuff
 *
 * @param <C>
 * @param <E>
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class CollectionStuff<C extends Collection<E>, E> {

    public C listReduplication(C c, int fieldIndex, boolean isDeclared) {

        if (c == null || c.size() == 0) return c;

        Class<?> clazz = ((ArrayList<?>) c).get(0).getClass();
        Field[] fs;
        if (isDeclared) fs = clazz.getDeclaredFields();
        else fs = clazz.getFields();

        int len = fs.length;
        if (len == 0 || len <= fieldIndex)
            throw new RuntimeException("Refection Exception[fields length is not enough]!");

        Field f = fs[fieldIndex];
        if (isDeclared) f.setAccessible(true);

        Map<Object, E> map = new HashMap<>(c.size());
        for (E e : c) {
            Object key;
            try {
                key = f.get(e);
                map.put(key, e);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException("Refection Exception[" + e1.getMessage() + "]!");
            }
        }
        return (C) map.values();
    }
}
