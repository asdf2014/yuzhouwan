package com.yuzhouwan.site.algorithms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Empty array vs. Null array
 *
 * @author Benedict Jin
 * @since 2015/12/29 0029
 */
public class EmptyArrayVSNullArray {

    @Test
    public void test() throws Throwable {
        Object[] os = new Object[0];
        /**
         * [Ljava.lang.Object;@75bd9247
         */
        System.out.println(os);
        int len = os.length;
        System.out.println("Length: " + len);
        assertEquals(0, len);
        System.out.println("Finalize with GC.");
//        os.finalize();
    }

}
