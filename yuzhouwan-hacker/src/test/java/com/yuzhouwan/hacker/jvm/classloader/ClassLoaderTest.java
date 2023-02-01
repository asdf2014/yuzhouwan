package com.yuzhouwan.hacker.jvm.classloader;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：ClassLoader Test
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class ClassLoaderTest {

    private static final String CLASS_LOADER_TEST_CLASS_NAME = ClassLoaderTest.class.getName();

    @Test
    public void testClassLoaderSimpleExample() throws Exception {
        ClassLoader mineClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String classFileName = name.substring(name.lastIndexOf(".") + 1).concat(".class");
                InputStream is = getClass().getResourceAsStream(classFileName);
                if (is == null) return super.loadClass(name);
                try {
                    byte[] bytes = new byte[is.available()];
                    return defineClass(name, bytes, 0, is.read(bytes));
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }
        };
        Object obj = mineClassLoader.loadClass(CLASS_LOADER_TEST_CLASS_NAME).newInstance();
        assertEquals(CLASS_LOADER_TEST_CLASS_NAME, obj.getClass().getName());
        assertFalse(obj instanceof ClassLoaderTest);
    }
}
