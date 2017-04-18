package com.yuzhouwan.hacker.effective;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan
 * All right reserved.
 * Function：Finally Test
 *
 * @author Benedict Jin
 * @since 2017/4/18
 */
public class FinallyTest {

    @Test
    public void testReturnInside() throws Exception {
        Exception catchE = null;
        try {
            someExceptionWrong();
        } catch (Exception e) {
            catchE = e;
        }
        assertEquals(true, catchE == null);

        try {
            someExceptionRight();
        } catch (Exception e) {
            catchE = e;
        }
        assertEquals("java.lang.RuntimeException: Some Problem.", catchE.getMessage());
    }

    private void someExceptionWrong() {
        try {
            throw new RuntimeException("Some Problem.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            return;
        }
    }

    private void someExceptionRight() {
        try {
            throw new RuntimeException("Some Problem.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
}
