package com.yuzhouwan.log.hadoop.reverse.index;

import org.junit.Test;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: InverseIndexByKeywordCombiner Tester
 *
 * @author Benedict Jin
 * @since 2016/3/31 0023
 */
public class InverseIndexByKeywordCombinerTest {

    @Test
    public void test() {
        int index = "a,b,1".lastIndexOf(',');
        //a,b,2
        System.out.println("a,b,1".substring(0, index + 1) + 2);
    }
}
