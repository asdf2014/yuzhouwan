package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.*;

import static com.yuzhouwan.common.util.CollectionUtils.intersection;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Collection Util Tester
 *
 * @author Benedict Jin
 * @since 2016/6/12
 */
public class CollectionUtilsTest {

    @Test
    public void removeAllByStrWithSeparator() throws Exception {

        assertEquals(Arrays.asList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a,c", ","));
        assertEquals(Arrays.asList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a", ","));
        assertEquals(Arrays.asList("aaa", "bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "c", ","));
        assertEquals(Arrays.asList(),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "", ","));

        assertEquals(Arrays.asList(""),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList(""), "a", ","));
    }

    @Test
    public void duplicate() throws Exception {
        {
            Integer[] a = new Integer[]{1, 2, 3};
            Integer[] b = new Integer[]{3};
            Integer[] c = new Integer[]{4};
            Integer[] d = null;
            assertEquals(3, intersection(a, b)[0]);
            assertEquals(0, intersection(b, c).length);
            assertEquals(true, intersection(c, d) == null);
        }
        {
            List<String> a = new LinkedList<>();
            a.add("a");
            a.add("c");
            List<String> b = new LinkedList<>();
            b.add("a");
            b.add("b");
            b.add("c");
            Collection<String> result = intersection(a, b);
            Iterator iterator = result.iterator();
            assertEquals(2, result.size());
            assertEquals("a", iterator.next());
            assertEquals("c", iterator.next());
        }
    }

    @Test
    public void getDuplicate() throws Exception {

        A _a = new A(1, "2", 3);
        A _b = new A(1, "a", 5);
        A _c = new A(4, "!", 3);

        A _d = new A(4, "2", 5);

        LinkedList<A> aList = new LinkedList<>();
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        assertEquals(_a, CollectionUtils.getDuplicate(aList, _d, "b", String.class));
        assertEquals(true, 2 == aList.size());
        assertEquals(_c, CollectionUtils.getDuplicate(aList, _d, "a", Integer.class));
        assertEquals(true, 1 == aList.size());
        assertEquals(_b, CollectionUtils.getDuplicate(aList, _d, "c", Object.class));
        assertEquals(true, 0 == aList.size());
        assertEquals(null, CollectionUtils.getDuplicate(aList, _d, "d", Object.class));
        assertEquals(true, 0 == aList.size());
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        assertEquals(null, CollectionUtils.getDuplicate(aList, _d, "d", Object.class));
        assertEquals(true, 3 == aList.size());
    }

    @Test
    public void removeCollTest() throws Exception {
        A _a = new A(1, "2", 3);
        A _b = new A(1, "a", 3);
        A _c = new A(1, "!", 3);

        LinkedList<A> aList = new LinkedList<>();
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        try {
            aList.remove(0);
            aList.remove(2);
            aList.remove(1);
            assertEquals(true, aList.size() == 0);
        } catch (Exception e) {
            assertEquals(true, e instanceof IndexOutOfBoundsException);
        }
    }

    private class A {
        int a;
        String b;
        Object c;

        A(int a, String b, Object c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
