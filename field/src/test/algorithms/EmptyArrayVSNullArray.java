package algorithms;

import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Empty array vs. Null array
 *
 * @author jinjy
 * @since 2015/12/29 0029
 */
public class EmptyArrayVSNullArray {

    @Test
    public void test() throws Throwable {
        Object[] os = new Object[0];
        /**
         * [Ljava.lang.Object;@75bd9247
         */
        System.out.println(os);
        System.out.println("Length: " + os.length);
        System.out.println("Finalize with GC.");
//        os.finalize();
    }

    @Test
    public void testNull(){
        Object[] os = null;
    }

    @Test
    public void testEmpty(){
        Object[] os = new Object[0];
    }

}
