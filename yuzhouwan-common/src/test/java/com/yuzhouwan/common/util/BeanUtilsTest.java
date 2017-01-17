package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import static com.yuzhouwan.common.util.CollectionUtils.remove;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
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

        assertEquals("{\"aA\":1,\"bB\":2,\"cC\":3,\"d_D\":{\"$ref\":\"@\"}}", beanA.toString());
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
            assertEquals(true, head.size() == 2);
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
            assertEquals(true, head.size() == 1);
            assertEquals(1, head.get(0).getaA());
        }
    }

    @Test
    public void treeSetTest() throws Exception {
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
    public void testColumn2Row() throws Exception {
        BeanA bean1 = new BeanA();
        BeanUtils.swapper(bean1, "aA", 1, "");
        BeanUtils.swapper(bean1, "bB", 2L, "_");
        BeanUtils.swapper(bean1, "c_C", 3d, "_");
        BeanUtils.swapper(bean1, "dD", bean1, "_");
        BeanA bean2 = new BeanA();
        BeanUtils.swapper(bean2, "aA", 4, "");
        BeanUtils.swapper(bean2, "bB", 5L, "_");
        BeanUtils.swapper(bean2, "c_C", 6d, "_");
        BeanUtils.swapper(bean2, "dD", bean2, "_");
        LinkedList<BeanA> beans = new LinkedList<>();
        beans.add(bean1);
        beans.add(bean2);
        String[] fields = {"aA"};
        LinkedList<String> rows = BeanUtils.columns2Row(beans, fields, true, false, "d_D", "this$0");
        assertEquals(true, rows.size() == 4);
        int count = 0;
        for (String row : rows)
            if (row.equals("{\"aA\":1,\"bB\":2}") || row.equals("{\"aA\":1,\"cC\":3}")
                    || row.equals("{\"aA\":4,\"bB\":5}") || row.equals("{\"aA\":4,\"cC\":6}")) count++;
        assertEquals(true, count == 4);
    }

    @Test
    public void getAllFieldsTest() throws Exception {
        assertEquals(5, BeanUtils.getAllFields(BeanB.class).size());
    }

    private class BeanB extends BeanA {
        private String e;
    }

    private class BeanA {
        private int aA;
        public long bB;
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