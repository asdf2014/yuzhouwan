package com.yuzhouwan.hacker.javaProhibited.mock;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;


/**
 * Created by Benedict Jin on 2015/9/15.
 */
public class TryMock {

    @Test
    public void mockTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        ComplexClass cc = mock(ComplexClass.class);
        Method m = ComplexClass.class.getDeclaredMethod("sayE", String.class);
        m.setAccessible(true);
        /**
         * U cannot set the value of 'BatchE' in ComplexClass with Mockito.
         */
        m.invoke(cc, "exception");
    }

}
