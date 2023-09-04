package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import static com.yuzhouwan.common.util.CollectionUtils.remove;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils Test
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public class BeanUtilsTest {

    @Test
    public void testSwapper() {

        BeanA beanA = new BeanA();
        BeanUtils.swapper(beanA, "aA", 1, "");
        BeanUtils.swapper(beanA, "bB", 2L, "_");
        BeanUtils.swapper(beanA, "c_C", 3d, "_");
        BeanUtils.swapper(beanA, "dD", beanA, "_");

        assertEquals("{\"aA\":1,\"bB\":2,\"cC\":3.0,\"d_D\":{\"$ref\":\"@\"}}", beanA.toString());
    }

    @Test
    public void testRemoveIgnores() {
        {
            LinkedList<String> head = new LinkedList<>();
            head.add("a");
            head.add("b");
            head.add("c");
            head.add("d");
            remove(head, null, "b", "c");
            assertEquals(2, head.size());
            assertEquals("a", head.get(0));
            assertEquals("d", head.get(1));
        }
        {
            LinkedList<BeanA> head = new LinkedList<>();
            BeanA bean1 = new BeanA();
            BeanUtils.swapper(bean1, "aA", 1, "");
            BeanUtils.swapper(bean1, "bB", 2L, "_");
            BeanA bean2 = new BeanA();
            BeanUtils.swapper(bean2, "aA", 3, "");
            BeanUtils.swapper(bean2, "bB", 4L, "_");
            head.add(bean1);
            head.add(bean2);
            remove(head, "bB", 4L);
            assertEquals(1, head.size());
            assertEquals(1, head.get(0).getaA());
        }
    }

    @Test
    public void treeSetTest() {
        TreeSet<Integer> t = new TreeSet<>();
        t.add(3);
        t.add(1);
        t.add(2);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> iterator = t.descendingIterator();
        while (iterator.hasNext()) stringBuilder.append(iterator.next());
        assertEquals("321", stringBuilder.toString());
    }

    @Test
    public void getAllFieldsTest() {
        assertEquals(5, BeanUtils.getFields(BeanB.class, BeanB.class.getName()).size());
        assertEquals(5, BeanUtils.getAllFields(BeanB.class).size());
    }

    private class BeanB extends BeanA {
        private String e;
    }

    private static class BeanA {
        public long bB;
        private int aA;
        private Double cC;
        private Object d_D;

        public int getaA() {
            return aA;
        }

        public Double getcC() {
            return cC;
        }

        public Object getD_D() {
            return d_D;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}