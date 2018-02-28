package com.yuzhouwan.hacker.jvm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
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
            assertEquals(true, A.class.isAssignableFrom(B.class));
            assertEquals(true, B.class.isAssignableFrom(C.class));
            assertEquals(true, A.class.isAssignableFrom(C.class));
            assertEquals(true, A.class.getClassLoader().equals(B.class.getClassLoader()));
            assertEquals(true, B.class.getClassLoader().equals(C.class.getClassLoader()));
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
        assertEquals(false, failed);
        failed = false;
        try {
            C c = (C) B.class.newInstance();
        } catch (Exception e) {
            failed = true;
        }
        assertEquals(true, failed);
    }
}

interface A {
}

abstract class B implements A {
}

class C extends B {
}
