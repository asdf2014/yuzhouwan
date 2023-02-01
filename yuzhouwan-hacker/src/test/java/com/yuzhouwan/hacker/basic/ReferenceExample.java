package com.yuzhouwan.hacker.basic;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Copyright @ 2023 yuzhouwan.com
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
        assertNotEquals(0L, (long) a.a);
        assertEquals(1L, (long) b.a);

        A c = new A();
        c.a = 2L;
        A d = (A) c.clone();
        d.a = 3L;
        assertEquals(2L, (long) c.a);
        assertEquals(3L, (long) d.a);

        {
            LinkedList<A> listA = new LinkedList<>();
            LinkedList<A> listB = new LinkedList<>();
            listA.add(a);
            listA.add(b);
            assertEquals(2, listA.size());
            listB.addAll(listA);
            assertEquals(2, listA.size());
            assertEquals(2, listB.size());
            listA.add(c);
            listA.add(d);
            assertEquals(4, listA.size());
            assertEquals(2, listB.size());
        }
    }
}
