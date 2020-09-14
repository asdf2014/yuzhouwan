package com.yuzhouwan.hacker.simd;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Thread;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：SIMD Benchmark
 *
 * @author Benedict Jin
 * @since 2019-01-04
 */
@State(Thread)
@OutputTimeUnit(NANOSECONDS)
@BenchmarkMode(AverageTime)
public class SIMDBenchmark {

    private static final int SIZE = 1024;

    private static int getHashPosition(long rawHash, int mask) {
        rawHash ^= rawHash >>> 33;
        rawHash *= 0xff51afd7ed558ccdL;
        rawHash ^= rawHash >>> 33;
        rawHash *= 0xc4ceb9fe1a85ec53L;
        rawHash ^= rawHash >>> 33;

        return (int) (rawHash & mask);
    }

    private static long getHashPositionMangle(long rawHash) {
        return rawHash ^ (rawHash >>> 33);
    }

    private static long getHashPositionMul1(long rawHash) {
        return rawHash * 0xff51afd7ed558ccdL;
    }

    private static long getHashPositionMul2(long rawHash) {
        return rawHash * 0xc4ceb9fe1a85ec53L;
    }

    private static int getHashPositionCast(long rawHash) {
        return (int) rawHash;
    }

    private static int getHashPositionMask(int rawHash) {
        return rawHash & 1048575;
    }

    private static long getHashPositionTwoInstructions(long rawHash) {
        rawHash ^= rawHash >>> 33;
        return rawHash * 0xff51afd7ed558ccdL;
    }

    /*
    -XX:+UseSuperWord
    Benchmark                              Mode  Cnt     Score    Error  Units
    SIMDBenchmark.bitshift                 avgt   10   742.504 ± 23.403  ns/op
    SIMDBenchmark.hashLongLoop             avgt   10  1483.138 ± 38.309  ns/op
    SIMDBenchmark.hashLongLoopSplit        avgt   10  1815.932 ± 48.107  ns/op
    SIMDBenchmark.hashLoopTwoInstructions  avgt   10   674.064 ± 13.250  ns/op
    SIMDBenchmark.increment                avgt   10   135.515 ±  2.763  ns/op

    -XX:-UseSuperWord
    Benchmark                              Mode  Cnt     Score     Error  Units
    SIMDBenchmark.bitshift                 avgt   10   723.457 ±  12.261  ns/op
    SIMDBenchmark.hashLongLoop             avgt   10  1589.289 ± 166.065  ns/op
    SIMDBenchmark.hashLongLoopSplit        avgt   10  3367.234 ± 113.719  ns/op
    SIMDBenchmark.hashLoopTwoInstructions  avgt   10   709.131 ±  33.971  ns/op
    SIMDBenchmark.increment                avgt   10   513.091 ±  14.065  ns/op
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(SIMDBenchmark.class.getSimpleName())
                .forks(1)
                .jvmArgsAppend(
                        "-XX:-UseSuperWord",
                        "-XX:+UnlockDiagnosticVMOptions",
                        "-XX:CompileCommand=print,*SIMDBenchmark.bitshift")
                .warmupIterations(5)
                .measurementIterations(10)
                .threads(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public void increment(Context context) {
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = context.values[i] + 1;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public void bitshift(Context context) {
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = context.values[i] / 2;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public int[] hashLongLoop(Context context) {
        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPosition(context.values[i], 1048575);
        }

        return context.results;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public int[] hashLongLoopSplit(Context context) {
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionMangle(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionMul1(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionMangle(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionMul2(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionMangle(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPositionCast(context.values[i]);
        }
        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPositionMask(context.results[i]);
        }

        return context.results;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public long[] hashLoopTwoInstructions(Context context) {
        for (int i = 0; i < SIZE; i++) {
            context.temporary[i] = getHashPositionTwoInstructions(context.values[i]);
        }
        return context.temporary;
    }

    @State(Thread)
    public static class Context {
        public final int[] results = new int[SIZE];
        private final long[] values = new long[SIZE];
        private final long[] temporary = new long[SIZE];

        @Setup
        public void setup() {
            Random random = new Random();
            for (int i = 0; i < SIZE; i++) {
                values[i] = random.nextLong() % (Long.MAX_VALUE / 32L);
            }
        }
    }
}
