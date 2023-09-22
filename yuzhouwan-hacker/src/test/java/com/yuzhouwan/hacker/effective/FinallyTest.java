package com.yuzhouwan.hacker.effective;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Test cases about Finally
 *
 * @author Benedict Jin
 * @since 2017/4/18
 */
public class FinallyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinallyTest.class);

    @Test
    public void testReturnInside() {
        Exception catchE = null;
        try {
            someExceptionWrong();
        } catch (Exception e) {
            catchE = e;
        }
        assertTrue(catchE == null);

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

    @Test
    public void testReturnInside2() {
        /*
        2017-04-18 16:52:59.234 | ERROR | Some Problem. | com.yuzhouwan.hacker.effective.FinallyTest.someExceptionWrong2 | FinallyTest.java:68
        2017-04-18 16:52:59.253 | ERROR | Some Problem. | com.yuzhouwan.hacker.effective.FinallyTest.someExceptionRight2 | FinallyTest.java:78
         */
        someExceptionWrong2();
        someExceptionRight2();
    }

    private void someExceptionWrong2() {
        try {
            throw new RuntimeException("Some Problem.");
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        } finally {
            return;
        }
    }

    private void someExceptionRight2() {
        try {
            throw new RuntimeException("Some Problem.");
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        } finally {
        }
    }

    @Test
    public void testReturnInside3() {

        Exception catchE = null;
        try {
            someExceptionWrong3();
        } catch (Exception e) {
            catchE = e;
        }
        assertTrue(catchE == null);

        try {
            someExceptionRight3();
        } catch (Exception e) {
            catchE = e;
        }
        assertEquals("Some Problem.", catchE.getMessage());
    }

    private void someExceptionWrong3() {
        try {
            throw new RuntimeException("Some Problem.");
        } finally {
            return;
        }
    }

    private void someExceptionRight3() {
        try {
            throw new RuntimeException("Some Problem.");
        } finally {
        }
    }
}
