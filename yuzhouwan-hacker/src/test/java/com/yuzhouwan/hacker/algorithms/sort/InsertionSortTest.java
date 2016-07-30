package com.yuzhouwan.hacker.algorithms.sort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InsertionSort Tester.
 *
 * @author Benedict Jin
 * @version 1.0
 * @since <pre>九月 21, 2015</pre>
 */
public class InsertionSortTest {

    private static final Logger _log = LoggerFactory.getLogger(InsertionSortTest.class);

    private InsertionSort insertionSort;

    @Before
    public void before() throws Exception {
        insertionSort = new InsertionSort();
    }

    @After
    public void after() throws Exception {
        insertionSort = null;
    }

    /**
     * Method: insertionSort(int[] unSort)
     */
    @Test
    public void testInsertionSort() throws Exception {
        {
            int[] unSort = {3, 2, 1};
            int[] sort = insertionSort.insertionSort(unSort);
            for (int i : sort)
                _log.debug("{}", i);
            System.out.print("\r\n");
        }
        {
            int[] unSort = {1, 1, 1};
            int[] sort = insertionSort.insertionSort(unSort);
            for (int i : sort)
                _log.debug("{}", i);
            System.out.print("\r\n");
        }
        {
            int[] unSort = {1, 2, 3};
            int[] sort = insertionSort.insertionSort(unSort);
            for (int i : sort)
                _log.debug("{}", i);
            System.out.print("\r\n");
        }
    }

    @Test
    public void pressureTest() {

        final int ARRAY_SIZE = 10000;

        int[] sorted = new int[ARRAY_SIZE];
        int[] reversed = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++)
            reversed[ARRAY_SIZE - i - 1] = sorted[i] = i;
        {
            long begin = System.currentTimeMillis();
            insertionSort.insertionSort(sorted);
            long end = System.currentTimeMillis();
            _log.debug("Max: " + sorted[ARRAY_SIZE - 1] + ", and finished in " + (end - begin) + " millisecond\r\n");
        }
        {
            long begin = System.currentTimeMillis();
            insertionSort.insertionSort(reversed);
            long end = System.currentTimeMillis();
            _log.debug("Max: " + sorted[ARRAY_SIZE - 1] + ", and finished in " + (end - begin) + " millisecond\r\n");
        }
    }
}
