package com.yuzhouwan.site.spel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：MultiIPAddresses Tester
 *
 * @author Benedict Jin
 * @since 2016/3/15 0007
 */
public class SpringELTryTest {

    private SpringELTry springELTry;

    @Before
    public void init() {
        springELTry = new SpringELTry();
    }

    @Test
    public void testHello() throws Exception {
        assertEquals("Hello, World!", springELTry.hello());
    }

    @Test
    public void testRegular() throws Exception {
        assertEquals(true, springELTry.matchExpression());
    }
}
