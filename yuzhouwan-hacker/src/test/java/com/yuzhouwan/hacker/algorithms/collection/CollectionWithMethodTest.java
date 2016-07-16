package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Collection Stuff
 *
 * @author Benedict Jin
 * @since 2016/3/11 0030
 */
public class CollectionWithMethodTest {

    private CollectionWithMethod c = new CollectionWithMethod();

    @Test
    public void test() {
        c.doSomethingMethod();
    }

    @Test
    public void testNull() {

        List l = new ArrayList<>();
        l.add(null);
        l.add(1);
        for (Object o : l) {
            if (o == null)
                l.remove(o);
        }
        for (Object o : l) {
            System.out.println(o);
        }
    }

    @Test
    public void str() {

        System.out.println("a".compareTo("a"));
    }

    @Test
    public void strEqual() {
    }

}
