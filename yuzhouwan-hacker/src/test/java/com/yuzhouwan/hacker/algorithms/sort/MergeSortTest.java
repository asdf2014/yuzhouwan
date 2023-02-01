package com.yuzhouwan.hacker.algorithms.sort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: MergeSort Tester
 *
 * @author Benedict Jin
 * @since 2015/9/23
 */
public class MergeSortTest {

    private MergeSort mergeSort;

    @Before
    public void before() {
        mergeSort = new MergeSort();
    }

    @After
    public void after() {
        mergeSort = null;
    }

    /**
     * Method: transfer(int[] benefits)
     */
    @Test
    public void testTransfer() {

        int[] recoder = {100, 113, 110, 85, 105, 102, 86, 63, 81, 101, 94, 106, 101, 79, 94, 90, 97};
        int[] transferred = mergeSort.transfer(recoder);
        StringBuilder strBuilder = new StringBuilder();
        for (int t : transferred)
            strBuilder.append(t).append(" ");
        assertEquals("13 -3 -25 20 -3 -16 -23 18 20 -7 12 -5 -22 15 -4 7 ", strBuilder.toString());
    }

    /**
     * Method: maxBenefit(int[] benefits, int low, int mid, int high)
     */
    @Test
    public void testMaxBenefit() {

        // [7 - 10] day: [18, 20, -7, 12] benefit
        int[] transferred = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
        int len = transferred.length;
        int max = mergeSort.maxBenefit(transferred, 0, (len / 2), len - 1);
        assertEquals(43, max);
    }

    /**
     * Method: findMaximum(int[] benefits, int low, int high)
     */
    @Test
    public void testFindMaximum() {

        // [7 - 10] day: [18, 20, -7, 12] benefit
        int[] transferred = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
        int len = transferred.length;
        int max = mergeSort.findMaximum(transferred, 0, len - 1);
        System.out.print(max);
        assertEquals(43, max);
    }

    @Test
    public void pressureTest() {

        int[] transferred = new int[10000];
        int len = 10000;

        for (int i = 0; i < len; i++)
            transferred[i] = i + 1;

        System.out.println("data: [1 ~ " + transferred[9999] + "]");

        long begin = System.currentTimeMillis();
        int max = mergeSort.findMaximum(transferred, 0, 9999);
        long end = System.currentTimeMillis();

        System.out.print("Result: " + max + ", and finished in " + (end - begin) + " millisecond");
        assertEquals(50005000, max);
    }

    @Test
    public void originTest() {
        int[] transferred = new int[1000];
        int len = 1000;

        for (int i = 0; i < len; i++)
            transferred[i] = i + 1;

        System.out.println("data: [1 ~ " + transferred[999] + "]");

        long begin = System.currentTimeMillis();
        int max = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = i; j < 1000; j++) {
                int sum = 0;
                for (int n = i; n <= j; n++) {
                    sum += transferred[n];
                }
                if (max < sum) {
                    max = sum;
                }
            }
        }
        long end = System.currentTimeMillis();

        System.out.print("Result: " + max + ", and finished in " + (end - begin) + " millisecond");
        assertEquals(500500, max);
    }

    @Test
    public void simple() {
        {
            int[] unSort = {0};
            mergeSort.findMaximum(unSort, 0, 0);
        }
        {
            int[] unSort = {0, 1};
            mergeSort.findMaximum(unSort, 0, 1);
        }
        {
            int[] unSort = {0, 1, 2};
            mergeSort.findMaximum(unSort, 0, 2);
        }
    }

}
