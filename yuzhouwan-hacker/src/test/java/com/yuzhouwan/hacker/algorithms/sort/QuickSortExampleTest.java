package com.yuzhouwan.hacker.algorithms.sort;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Quick Sort Example Tester
 *
 * @author Benedict Jin
 * @since 2016/9/19
 */
public class QuickSortExampleTest {

    @Test
    public void quickSort() throws Exception {

        int[] unSort = new int[]{2, 3, 1};
        QuickSortExample.quickSort(unSort, 0, 2);

        StringBuilder strBuilder = new StringBuilder();
        for (int i : unSort) {
            strBuilder.append(i).append(" ");
        }
        assertEquals("1 2 3", strBuilder.toString().trim());
    }
}
