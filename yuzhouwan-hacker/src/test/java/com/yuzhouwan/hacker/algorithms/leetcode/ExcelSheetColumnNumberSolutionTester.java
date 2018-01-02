package com.yuzhouwan.hacker.algorithms.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Excel Sheet Column Number Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/10/13
 */
public class ExcelSheetColumnNumberSolutionTester {

    @Test
    public void titleToNumberTest() throws Exception {
        assertEquals(1, ExcelSheetColumnNumberSolution.titleToNumber("A"));
        assertEquals(2, ExcelSheetColumnNumberSolution.titleToNumber("B"));
        assertEquals(3, ExcelSheetColumnNumberSolution.titleToNumber("C"));
        assertEquals(26, ExcelSheetColumnNumberSolution.titleToNumber("Z"));

        assertEquals(27, ExcelSheetColumnNumberSolution.titleToNumber("AA"));
        assertEquals(28, ExcelSheetColumnNumberSolution.titleToNumber("AB"));
    }

    @Test
    public void simpleTest() throws Exception {
        assertEquals(1, ExcelSheetColumnNumberSolution.simple("A"));
        assertEquals(2, ExcelSheetColumnNumberSolution.simple("B"));
        assertEquals(3, ExcelSheetColumnNumberSolution.simple("C"));
        assertEquals(26, ExcelSheetColumnNumberSolution.simple("Z"));

        assertEquals(27, ExcelSheetColumnNumberSolution.simple("AA"));
        assertEquals(28, ExcelSheetColumnNumberSolution.simple("AB"));
    }
}
