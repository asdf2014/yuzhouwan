package com.yuzhouwan.hacker.effective;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Benedict Jin on 2016/5/30.
 */
public class GenericTest {

    private Generic gen;

    @Before
    public void init() {
        gen = new Generic();
    }

    @Test
    public void originTest() {
        System.out.println(gen.multiply(1, 1));
        System.out.println(gen.multiply(1d, 1d));
        System.out.println(gen.multiply(1f, 1f));
        System.out.println(Math.sqrt((int) gen.multiply('1', '1')));
    }

    @Test
    public void genericTest() {
        System.out.println(gen.multiply(1).getClass());
        System.out.println(gen.multiply(2f).getClass());
        System.out.println(gen.multiply(3d).getClass());
    }

    @Test
    public void classGenericTest() {
        Generic<String> g = new Generic<>();
        g.y = "1";
    }
}
