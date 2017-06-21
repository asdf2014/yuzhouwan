package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class CollectionStuffTest {

    private List<ComplexClass> cs;

    @Before
    public void init() {
        ComplexClass c0 = new ComplexClass();
        c0.setI(1);
        c0.setS("1");
        c0.setO(new CollectionStuffTest());

        ComplexClass c = new ComplexClass();
        c.setI(1);
        c.setS("1");
        c.setO(new CollectionStuffTest());

        cs = new ArrayList<>();
        cs.add(c0);
        cs.add(c);
    }


    /**
     * Method: listReduplication(C c, int fieldIndex, boolean isDeclared)
     */
    @Test
    public void testListReduplication() throws Exception {
        CollectionStuff<List<ComplexClass>, ComplexClass> collectionStuff = new CollectionStuff<>();
        Collection<ComplexClass> collection = collectionStuff.listReduplication(cs, 1, true);
        collection.forEach(System.out::println);
    }

    private class ComplexClass {
        private Integer i;
        private String s;
        private Object o;

        @Override
        public boolean equals(Object o1) {
            if (this == o1) return true;
            if (!(o1 instanceof ComplexClass)) return false;

            ComplexClass that = (ComplexClass) o1;

            if (!getI().equals(that.getI())) return false;
            if (!getS().equals(that.getS())) return false;
            return getO().equals(that.getO());

        }

        @Override
        public int hashCode() {
            int result = getI().hashCode();
            result = 31 * result + getS().hashCode();
            result = 31 * result + getO().hashCode();
            return result;
        }

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }
    }

    @Test
    public void testFastFail() throws Exception {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        while (list.size() > 0) {
            System.out.println(list.removeFirst());
        }
        assertEquals(0, list.size());
    }

    @Test
    public void testByteBufferMap() throws Exception {

        byte[] bytesA = {1, 2};
        byte[] bytesB = {2, 3};
        byte[] bytesC = {2, 3};

        ByteBuffer byteBufferA = ByteBuffer.wrap(bytesA);
        ByteBuffer byteBufferB = ByteBuffer.wrap(bytesB);
        ByteBuffer byteBufferC = ByteBuffer.wrap(bytesC);

        HashMap<ByteBuffer, String> map = new HashMap<>();
        map.put(byteBufferA, "A");
        map.put(byteBufferB, "B");
        assertEquals(true, map.containsKey(byteBufferC));
    }

    @Test
    public void testBoxMap() throws Exception {

        HashMap<Long, String> map = new HashMap<>();
        map.put(1L, "1L");
        map.put(2L, "2L");
        map.put(3L, "3L");
        map.put(4L, "4L");

        long key = 2;
        class A {
            public long a;
        }
        A a = new A();
        a.a = 4;
        assertEquals(null, map.get(1));
        assertEquals("2L", map.get(key));
        assertEquals("3L", map.get(3L));
        assertEquals("4L", map.get(a.a));
    }

}
