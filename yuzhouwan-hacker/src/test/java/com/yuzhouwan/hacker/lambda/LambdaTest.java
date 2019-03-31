package com.yuzhouwan.hacker.lambda;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Lambda Test
 *
 * @author Benedict Jin
 * @since 2018/3/5
 */
public class LambdaTest {

    private static List<Apple> apples = new LinkedList<>();

    @Before
    public void before() {
        release();
        init();
    }

    private void init() {
        apples.add(new Apple("A", "red", 10d));
        apples.add(new Apple("A", "red", 3d));
        apples.add(new Apple("A", "green", 8d));
        apples.add(new Apple("B", "yellow", 15d));
    }

    private void release() {
        apples.clear();
    }

    @Test
    public void filter() {
        List<Apple> hugeRedApples = apples.parallelStream()
                .filter((Apple a) -> "A".equals(a.getName()))
                .filter(a -> "red".equals(a.getColor()))
                .filter(b -> b.getWeight() > 5d)
                .collect(Collectors.toList());
        assertEquals(1, hugeRedApples.size());
        Apple a = hugeRedApples.get(0);
        assertEquals("A", a.getName());
        assertEquals("red", a.getColor());
        assertTrue(a.getWeight() == 10d);
    }

    @Test
    public void sort() {
        List<Apple> sorted = apples.parallelStream()
                .sorted((Apple a, Apple b) -> {
                    if (a.getWeight() > b.getWeight()) return 1;
                    else if (a.getWeight() < b.getWeight()) return -1;
                    else return 0;
                })
                .collect(Collectors.toList());
        assertEquals("[{\"color\":\"red\",\"name\":\"A\",\"weight\":3.0}," +
                        "{\"color\":\"green\",\"name\":\"A\",\"weight\":8.0}," +
                        "{\"color\":\"red\",\"name\":\"A\",\"weight\":10.0}," +
                        "{\"color\":\"yellow\",\"name\":\"B\",\"weight\":15.0}]",
                JSON.toJSONString(sorted));
    }

    @Test
    public void run() {
        new Thread(this::sort).start();
    }

    @Test
    public void after() {
        release();
        apples = null;
    }
}
