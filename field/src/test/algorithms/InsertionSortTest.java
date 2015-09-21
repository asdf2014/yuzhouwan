package algorithms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * InsertionSort Tester.
 *
 * @author <asdf>
 * @version 1.0
 * @since <pre>¾ÅÔÂ 21, 2015</pre>
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


} 
