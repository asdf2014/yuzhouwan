package algorithms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * InsertionSort Tester.
 *
 * @author <asdf2014>
 * @version 1.0
 * @since <pre>九月 21, 2015</pre>
 */
public class InsertionSortTest {

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
     * Method: insertionSort(int[] unsort)
     */
    @Test
    public void testInsertionSort() throws Exception {
        {
            int[] unsort = {3, 2, 1};
            int[] sort = insertionSort.insertionSort(unsort);
            for (int i : sort)
                System.out.print(i);
            System.out.print("\r\n");
        }
        {
            int[] unsort = {1, 1, 1};
            int[] sort = insertionSort.insertionSort(unsort);
            for (int i : sort)
                System.out.print(i);
            System.out.print("\r\n");
        }
        {
            int[] unsort = {1, 2, 3};
            int[] sort = insertionSort.insertionSort(unsort);
            for (int i : sort)
                System.out.print(i);
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
            System.out.print("Max: " + sorted[ARRAY_SIZE - 1] + ", and finished in " + (end - begin) + " millisecond\r\n");
        }
        {
            long begin = System.currentTimeMillis();
            insertionSort.insertionSort(reversed);
            long end = System.currentTimeMillis();
            System.out.print("Max: " + sorted[ARRAY_SIZE - 1] + ", and finished in " + (end - begin) + " millisecond\r\n");
        }
    }

}
