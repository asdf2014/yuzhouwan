package com.yuzhouwan.hacker.basic;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
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
        assertEquals(false, a.a == 0L);
        assertEquals(true, b.a == 1L);

        A c = new A();
        c.a = 2L;
        A d = (A) c.clone();
        d.a = 3L;
        assertEquals(true, c.a == 2L);
        assertEquals(true, d.a == 3L);

        {
            LinkedList<A> listA = new LinkedList<>();
            LinkedList<A> listB = new LinkedList<>();
            listA.add(a);
            listA.add(b);
            assertEquals(true, listA.size() == 2);
            listB.addAll(listA);
            assertEquals(true, listA.size() == 2);
            assertEquals(true, listB.size() == 2);
            listA.add(c);
            listA.add(d);
            assertEquals(true, listA.size() == 4);
            assertEquals(true, listB.size() == 2);
        }
    }
}
