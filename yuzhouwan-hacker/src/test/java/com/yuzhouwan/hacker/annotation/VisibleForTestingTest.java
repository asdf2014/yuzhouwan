package com.yuzhouwan.hacker.annotation;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：VisibleForTesting Test
 *
 * @author Benedict Jin
 * @since 2018/2/6
 */
public class VisibleForTestingTest {

    @Test
    public void testVisibleForTesting() {
        assertTrue(new VisibleForTestingExample().visibleForTesting());
    }
}
