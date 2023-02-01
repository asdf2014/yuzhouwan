package com.yuzhouwan.hacker.jvm;

import org.junit.Test;

import static org.junit.Assert.*;

interface A {
}

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Class Test
 *
 * @author Benedict Jin
 * @since 2018/2/25
 */
public class ClassTest {

    @Test
    public void isAssignableFromTest() {
        {
            // If those classes were hold by different classloaders will got the `false` result,
            // Then we can solve that by serializing.
            assertTrue(A.class.isAssignableFrom(B.class));
            assertTrue(B.class.isAssignableFrom(C.class));
            assertTrue(A.class.isAssignableFrom(C.class));
            assertEquals(A.class.getClassLoader(), B.class.getClassLoader());
            assertEquals(B.class.getClassLoader(), C.class.getClassLoader());
        }
        {
            Class<? extends Class> aClass = A.class.getClass();
            Class<? extends Class> bClass = B.class.getClass();
        }
    }

    @Test
    public void newInstanceTest() {
        boolean failed = false;
        try {
            B b = (B) C.class.newInstance();
        } catch (Exception e) {
            failed = true;
        }
        assertFalse(failed);
        failed = false;
        try {
            C c = (C) B.class.newInstance();
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);
    }
}

abstract class B implements A {
}

class C extends B {
}
