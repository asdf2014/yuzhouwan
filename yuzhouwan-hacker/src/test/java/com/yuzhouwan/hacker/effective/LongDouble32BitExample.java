package com.yuzhouwan.hacker.effective;

import org.junit.Test;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Long/Double 32Bit
 *
 * @author Benedict Jin
 * @since 2017/5/31
 */
public class LongDouble32BitExample {

    // -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=longdouble32bit.log
    @Test
    public void value() throws Exception {
        /*
        0: lconst_1
        1: lstore_1
        2: ldc2_w          #2   // double 2.0d
        5: dstore_3
        6: return
         */
        long l = 1L;
        double d = 2D;
    }
}
