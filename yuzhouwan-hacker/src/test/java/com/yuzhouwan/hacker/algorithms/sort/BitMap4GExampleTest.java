package com.yuzhouwan.hacker.algorithms.sort;

import org.junit.Test;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: BitMap4G Example Tester
 *
 * @author Benedict Jin
 * @since 2016/8/2
 */
public class BitMap4GExampleTest {

    @Test
    public void test() {

        BitMap4GExample bitMap4GExample = new BitMap4GExample();

        int a[] = {5, 8, 7, 6, 3, 1, 10, 78, 56, 34, 23, 12, 43, 54, 65, 76,
                87, 98, 89, 100};
        int i;

        bitMap4GExample.bitmapInit(100, 0);
        for (i = 0; i < a.length; i++)
            bitMap4GExample.bitmapSet(a[i]);

        for (i = 0; i <= 100; i++)
            if (bitMap4GExample.bitmapGet(i) > 0)
                System.out.print(bitMap4GExample.bitmapIndex(i) + ", ");
        bitMap4GExample.bitmapFree();
    }
}
