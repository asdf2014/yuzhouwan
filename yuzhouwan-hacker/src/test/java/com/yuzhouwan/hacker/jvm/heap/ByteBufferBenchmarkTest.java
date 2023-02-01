package com.yuzhouwan.hacker.jvm.heap;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：ByteBuffer Benchmark Test
 *
 * @author Benedict Jin
 * @since 2018-11-19
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ByteBufferBenchmarkTest {

    @Param({"1", "100", "1000", "5000", "10000", "100000"})
    private int n;

    private ByteBuffer allocate;
    private ByteBuffer allocateDirect;

    /*
    Benchmark                                  (n)   Mode  Cnt      Score        Error   Units
    ByteBufferBenchmarkTest.allocate             1  thrpt    3  73353.747 ± 204162.465  ops/ms
    ByteBufferBenchmarkTest.allocate           100  thrpt    3  51179.651 ± 304426.641  ops/ms
    ByteBufferBenchmarkTest.allocate          1000  thrpt    3   5991.300 ±  18072.980  ops/ms
    ByteBufferBenchmarkTest.allocate          5000  thrpt    3   1576.504 ±   6040.311  ops/ms
    ByteBufferBenchmarkTest.allocate         10000  thrpt    3    888.931 ±   5976.162  ops/ms
    ByteBufferBenchmarkTest.allocate        100000  thrpt    3     97.778 ±    727.435  ops/ms
    ByteBufferBenchmarkTest.allocateDirect       1  thrpt    3   1051.676 ±   2990.740  ops/ms
    ByteBufferBenchmarkTest.allocateDirect     100  thrpt    3   1072.422 ±   6368.776  ops/ms
    ByteBufferBenchmarkTest.allocateDirect    1000  thrpt    3    934.542 ±   9721.756  ops/ms
    ByteBufferBenchmarkTest.allocateDirect    5000  thrpt    3    232.323 ±   1643.966  ops/ms
    ByteBufferBenchmarkTest.allocateDirect   10000  thrpt    3    218.939 ±   1669.600  ops/ms
    ByteBufferBenchmarkTest.allocateDirect  100000  thrpt    3     24.372 ±    125.365  ops/ms
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ByteBufferBenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .threads(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void allocate() {
        allocate = ByteBuffer.allocate(n);
    }

    @Benchmark
    public void allocateDirect() {
        allocateDirect = ByteBuffer.allocateDirect(n);
    }

    @TearDown
    public void teardown() {
        if (allocate != null) {
            allocate.clear();
        }
        if (allocateDirect != null) {
            allocateDirect.clear();
        }
    }
}
