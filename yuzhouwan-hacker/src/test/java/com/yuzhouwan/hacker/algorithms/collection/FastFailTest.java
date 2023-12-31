package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: FastFail Test
 *
 * @author Benedict Jin
 * @since 2016/8/1
 */
public class FastFailTest {

    @Test
    public void collectionRemove() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", "A");
        parameters.put("b", 2);
        parameters.put("c", new String[]{"2"});

        {
            assertEquals(3, parameters.size());

            parameters.remove("b");
            assertEquals(2, parameters.size());
        }
        {
            // fast fail, just happened when while/for/advance for, can fix it by iterator
            try {
                parameters.keySet().forEach(key -> {
                    if ("c".equals(key)) {
                        parameters.remove(key);
                    }
                });
            } catch (Exception cme) {
                assertEquals(ConcurrentModificationException.class, cme.getClass());
            }
        }
        {
            Iterator<String> iterator = parameters.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                if ("c".equals(key)) {
                    parameters.remove(key);
                }
                assertEquals(1, parameters.size());
            }
        }
    }
}
