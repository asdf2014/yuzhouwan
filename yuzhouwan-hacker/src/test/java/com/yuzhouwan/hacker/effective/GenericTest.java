package com.yuzhouwan.hacker.effective;

import com.yuzhouwan.hacker.algorithms.collection.sort.Company;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Generic Tester
 *
 * @author Benedict Jin
 * @since 2016/5/30
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

    @Test
    public void operationOrder() {
        int calc = 0;
        int calcEnd = 1;
        int calcBegin = 2;
        calc += calcEnd - calcBegin;
        if (calc < 0) calc = -calc;
        assertEquals(1, calc);
    }

    @Test
    public void date() {
        int month = 201512;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt((month + "").substring(4)));
        c.set(Calendar.YEAR, Integer.parseInt((month + "").substring(0, 4)));
        System.out.println(c.getTime());
    }

    @Test
    public void mapModify() {
        List<Company> companies = new LinkedList<>();
        companies.add(new Company("1", 1));
        companies.add(new Company("2", 2));
        companies.add(new Company("3", 3));
        for (Company company : companies) {
            company.setFoundation(4);
        }
        for (Company company : companies) {
            assertEquals(true, company.getFoundation() == 4);
        }
    }
}
