package com.yuzhouwan.hacker.basic;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Reference Example
 *
 * @author Benedict Jin
 * @since 2017/6/2
 */
public class ReferenceExample {

    @Test
    public void referenceTest() throws Exception {

        class A implements Cloneable {
            Long a;

            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        A a = new A();
        a.a = 0L;
        A b = a;
        b.a = 1L;
        assertFalse(a.a == 0L);
        assertTrue(b.a == 1L);

        A c = new A();
        c.a = 2L;
        A d = (A) c.clone();
        d.a = 3L;
        assertTrue(c.a == 2L);
        assertTrue(d.a == 3L);

        {
            LinkedList<A> listA = new LinkedList<>();
            LinkedList<A> listB = new LinkedList<>();
            listA.add(a);
            listA.add(b);
            assertTrue(listA.size() == 2);
            listB.addAll(listA);
            assertTrue(listA.size() == 2);
            assertTrue(listB.size() == 2);
            listA.add(c);
            listA.add(d);
            assertTrue(listA.size() == 4);
            assertTrue(listB.size() == 2);
        }
    }
}
