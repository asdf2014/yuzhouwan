package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * Method: listDeduplication(C c, int fieldIndex, boolean isDeclared)
     */
    @Test
    public void testListDeduplication() throws Exception {
        CollectionStuff<List<ComplexClass>, ComplexClass> collectionStuff = new CollectionStuff<>();
        Collection<ComplexClass> collection = collectionStuff.listDeduplication(cs, 1, true);
        for (ComplexClass complexClass : collection) {
            System.out.println(complexClass);
        }
    }

    class ComplexClass {
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

}