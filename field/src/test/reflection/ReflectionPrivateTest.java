package reflection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：ReflectionPrivate Tester.
 *
 * @author asdf2014
 * @since 2015/11/16
 */
public class ReflectionPrivateTest {

    private ReflectionPrivate reflectionPrivate;

    @Before
    public void before() throws Exception {
        reflectionPrivate = new ReflectionPrivate();
    }

    @After
    public void after() throws Exception {
        reflectionPrivate = null;
    }

    /**
     * Method: createSimpleBean(Integer i, Long l, String s)
     */
    @Test
    public void testCreateSimpleBean() throws Exception {
        System.out.println(new SimpleBean(1, 2L, "3"));
        System.out.println(reflectionPrivate.createSimpleBean(1, 2L, "3"));
    }

} 
