package com.yuzhouwan.site.algorithms;

import com.yuzhouwan.site.hacker.algorithms.MergeSort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MergeSort Tester.
 *
 * @author <asdf2014>
 * @version 1.0
 * @since <pre>9, 23, 2015</pre>
 */
public class MergeSortTest {

    private MergeSort mergeSort;

    @Before
    public void before() throws Exception {
        mergeSort = new MergeSort();
    }

    @After
    public void after() throws Exception {
        mergeSort = null;
    }

    /**
     * Method: transfer(int[] benefits)
     */
    @Test
    public void testTransfer() throws Exception {

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
    public void testMaxBenefit() throws Exception {

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
    public void testFindMaximum() throws Exception {

        // [7 - 10] day: [18, 20, -7, 12] benefit
        int[] transferred = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
        int len = transferred.length;
        int max = mergeSort.findMaximum(transferred, 0, len - 1);
        System.out.print(max);
        assertEquals(43, max);
    }

    @Test
    public void pressureTest() throws Exception {

        int[] transferred = new int[10000];
        int len = 10000;

        for (int i = 0; i < len; i++)
            transferred[i] = i + 1;

        System.out.println("data: [1 ~ " + transferred[9999] + "]");

        long begin = System.currentTimeMillis();
        int max = mergeSort.findMaximum(transferred, 0, 9999);
        long end = System.currentTimeMillis();

        System.out.print("Result: " + max + ", and finished in " + (end - begin) + " millisecond\r\n");
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
            int[] unsort = {0};
            mergeSort.findMaximum(unsort, 0, 0);
        }
        {
            int[] unsort = {0, 1};
            mergeSort.findMaximum(unsort, 0, 1);
        }
        {
            int[] unsort = {0, 1, 2};
            mergeSort.findMaximum(unsort, 0, 2);
        }
    }

}
