package com.yuzhouwan.hacker.algorithms.reflection;

import com.yuzhouwan.hacker.json.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：ReflectionPrivate Tester.
 *
 * @author Benedict Jin
 * @since 2015/11/16
 */
public class ReflectionPrivateTest {

    private ReflectionPrivate reflectionPrivate;

    @Before
    public void before() throws Exception {
        reflectionPrivate = new ReflectionPrivate();
    }

    @After
    public void after() throws Exception {
        reflectionPrivate = null;
    }

    /**
     * Method: createSimpleBean(Integer i, Long l, String s)
     */
    @Test
    public void testCreateSimpleBean() throws Exception {
        System.out.println(new SimpleBean(1, 2L, "3"));
        System.out.println(reflectionPrivate.createSimpleBean(1, 2L, "3"));
    }

    @Test
    public void testReflection() throws Exception {
        A a = new A("a", "b", "c", 4);
        Field f = a.getClass().getDeclaredField("rule");
        f.setAccessible(true);
        assertEquals("a", f.get(a).toString());

        f = a.getClass().getDeclaredField("mode");
        f.setAccessible(true);
        assertEquals("c", f.get(a).toString());
    }

} 
