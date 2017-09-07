package com.yuzhouwan.hacker.effective;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：LongAdder Example
 *
 * @author Benedict Jin
 * @since 2017/5/24
 */
public class LongAdderExample {

    @Test
    public void longAdder() throws Exception {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        longAdder.increment();
    }

    @Test
    public void atomicLong() throws Exception {
        new AtomicLong().incrementAndGet();
    }

    // -Xmx512M -Xms512M -Xmn256M -XX:+AlwaysPreTouch -ea
    // JIT: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=pressureLongAdder.log
    @Ignore
    @Test
    public void pressureLongAdder() throws Exception {
        final LongAdder longAdder = new LongAdder();
        ExecutorService executorService = Executors.newCachedThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Thread(() -> {
                for (int j = 0; j < 1000_0000; j++) {
                    longAdder.increment();
                }
                System.out.print(String.format("%s %s \t", Thread.currentThread().getId(), longAdder.longValue()));
                /*
                14 19607585 	12 36445036 	20 38985288 	38 76821270 	70 117094732 	18 127252576
                22 137043349 	26 153411172 	30 164051380 	34 165971155 	102 192241678 	134 201104979
                158 232657818 	46 279030056 	174 288502545 	94 347965290 	198 348060553 	118 348087414
                36 353092712 	28 357762215 	44 365464475 	126 379518198 	54 379623515 	182 380077075
                142 385263911 	78 389013887 	62 389085727 	110 389122678 	86 389920423 	166 393535019
                150 396382512 	190 403100499 	32 403161217 	208 403197689 	206 406065520 	16 410725026
                24 415347205 	40 415379997 	48 415733397 	104 418507295 	192 423244160 	176 455793362
                168 458311865 	160 463028656 	136 496375440 	72 541243645 	186 561877000 	170 575352229
                162 584152392 	154 604552121 	138 614092854 	64 638151890 	114 668705836 	58 669235250
                188 699213410 	156 729222401 	124 754336889 	100 784326386 	76 813479501 	120 827569944
                66 830236567 	98 832153503 	112 841408676 	204 849520891 	210 852391130 	202 864804732
                172 875603834 	194 877222893 	200 881090909 	88 882809513 	80 882846368 	56 887174571
                178 889682247 	140 901357028 	146 902169049 	184 904540678 	152 915608988 	130 917896629
                116 924616135 	144 927674541 	122 930399321 	128 939791111 	106 942656234 	84 950848174
                96 951904067 	90 954910184 	74 964338213 	196 966487766 	82 968307139 	52 975854400
                180 977385398 	164 978882525 	50 980896807 	148 988292352 	132 989090669 	108 996891232
                92 996921398 	42 996938988 	68 996953941 	60 1000000000
                 */
            }));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("\n" + (endTime - startTime));    // 3275 ms
    }

    // -Xmx512M -Xms512M -Xmn256M -XX:+AlwaysPreTouch -ea
    @Ignore
    @Test
    public void pressureAtomicLong() throws Exception {
        final AtomicLong atomicLong = new AtomicLong();
        ExecutorService executorService = Executors.newCachedThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Thread(() -> {
                for (int j = 0; j < 1000_0000; j++) {
                    atomicLong.getAndIncrement();
                }
                System.out.print(String.format("%s %s \t", Thread.currentThread().getId(), atomicLong.longValue()));
                /*
                12 390000000 	28 390000000 	44 390000000 	20 390000000 	26 390000000 	18 390000000
                80 390000000 	56 390000000 	96 390000000 	24 390000000 	88 390000000 	72 390000000
                22 390000000 	118 390000000 	54 390000000 	142 390000000 	70 390000000 	86 390000000
                182 390000000 	110 390000000 	62 390000000 	78 390000000 	102 390000000 	158 390000000
                150 390000000 	46 390000000 	38 390000000 	126 390000000 	94 390000000 	134 390000000
                14 390000000 	48 390000000 	40 390000000 	32 390000000 	34 390000000 	64 390000000
                42 390000000 	36 390000000 	16 390000000 	180 416396554 	204 419908287 	196 425536497
                92 732203658 	30 733835560 	202 733835559 	210 733873571 	146 733878564 	186 733883527
                170 733888686 	76 733892691 	84 733888815 	148 733901560 	162 733907032 	172 733908079
                52 733913280 	116 733918421 	124 733906868 	164 733920945 	132 733891348 	68 733923672
                108 733924928 	156 733926091 	60 733921998 	140 733927257 	188 733928891 	154 733871822
                194 733830477 	178 733872527 	100 733830322 	106 748251688 	144 1000000000 	98 1000000000
                58 1000000000 	90 1000000000 	130 1000000000 	138 1000000000 	114 1000000000 	104 1000000000
                168 1000000000 	200 1000000000 	184 1000000000 	160 1000000000 	174 1000000000 	112 1000000000
                190 1000000000 	198 1000000000 	82 1000000000 	206 1000000000 	166 1000000000 	176 1000000000
                136 1000000000 	208 1000000000 	74 1000000000 	122 1000000000 	152 1000000000 	192 1000000000
                120 1000000000 	128 1000000000 	66 1000000000 	50 1000000000
                 */
            }));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("\n" + (endTime - startTime));    // 19409 ms
    }
}
