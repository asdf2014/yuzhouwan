package com.yuzhouwan.bigdata.zookeeper.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Benchmark Simple
 *
 * @author Benedict Jin
 * @since 2017/8/9
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkSimple {

    private static int add(int a, int b) {
        return a + b;
    }

    /*
    Benchmark               Mode  Cnt         Score        Error   Units
    BenchmarkSimple.bench  thrpt    5  13352311.603 ± 767137.272  ops/ms
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkSimple.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(10)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void bench() {
        add(1, 1);
    }
}
