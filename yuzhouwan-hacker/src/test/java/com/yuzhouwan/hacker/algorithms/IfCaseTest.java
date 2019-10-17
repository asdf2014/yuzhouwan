package com.yuzhouwan.hacker.algorithms;

import org.junit.Test;

/**
 * IfCase Tester.
 *
 * @author Benedict Jin
 * @version 1.0
 * @since <pre>五月 5, 2015</pre>
 */
public class IfCaseTest {

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void test() {

        Boolean A = new Boolean(true);
        Boolean B = new Boolean(true);
        if (A != B && A.compareTo(B) == 0) {
            System.out.println("A: " + A + ", B: " + B);
        }
    }

}
