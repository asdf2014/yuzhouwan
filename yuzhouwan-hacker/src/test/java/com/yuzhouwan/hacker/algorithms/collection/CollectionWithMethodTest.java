package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Collection Stuff
 *
 * @author Benedict Jin
 * @since 2016/3/11
 */
public class CollectionWithMethodTest {

    private final CollectionWithMethod c = new CollectionWithMethod();

    @Test
    public void test() {
        c.doSomethingMethod();
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    public void testNull() {

        List l = new ArrayList<>();
        l.add(null);
        l.add(1);
        for (Object o : l) {
            if (o == null) {
                l.remove(o);
            }
        }
        for (Object o : l) {
            System.out.println(o);
        }
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void str() {
        Assert.assertEquals(0, "a".compareTo("a"));
    }

    @Test
    public void strEqual() {
    }
}
