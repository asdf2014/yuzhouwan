package com.yuzhouwan.hacker.algorithms.collection;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：toArray Benchmark
 *
 * @author Benedict Jin
 * @since 2018/7/9
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ToArrayBenchmark {

    private final List<Object> objList = new ArrayList<>();
    private final List<Integer> intList = new ArrayList<>();
    @Param({"1", "100", "1000", "5000", "10000", "100000"})
    private int n;

    /*
    Benchmark                       (n)  Mode  Cnt      Score      Error  Units
    ToArrayBenchmark.preSizeInt       1  avgt   20     22.217 ±    1.048  ns/op
    ToArrayBenchmark.preSizeInt     100  avgt   20     87.662 ±   41.051  ns/op
    ToArrayBenchmark.preSizeInt    1000  avgt   20    538.227 ±   55.423  ns/op
    ToArrayBenchmark.preSizeInt    5000  avgt   20   2636.566 ±  382.364  ns/op
    ToArrayBenchmark.preSizeInt   10000  avgt   20   6640.608 ±  758.939  ns/op
    ToArrayBenchmark.preSizeInt  100000  avgt   20  70372.662 ± 4088.575  ns/op
    ToArrayBenchmark.preSizeObj       1  avgt   20     21.616 ±    0.732  ns/op
    ToArrayBenchmark.preSizeObj     100  avgt   20     69.458 ±   12.221  ns/op
    ToArrayBenchmark.preSizeObj    1000  avgt   20    532.483 ±   90.526  ns/op
    ToArrayBenchmark.preSizeObj    5000  avgt   20   2381.348 ±  307.851  ns/op
    ToArrayBenchmark.preSizeObj   10000  avgt   20   5914.084 ±  488.912  ns/op
    ToArrayBenchmark.preSizeObj  100000  avgt   20  72402.852 ± 5248.494  ns/op
    ToArrayBenchmark.resizeInt        1  avgt   20     12.806 ±    1.017  ns/op
    ToArrayBenchmark.resizeInt      100  avgt   20     59.680 ±   13.333  ns/op
    ToArrayBenchmark.resizeInt     1000  avgt   20    563.573 ±   40.215  ns/op
    ToArrayBenchmark.resizeInt     5000  avgt   20   2684.939 ±  163.191  ns/op
    ToArrayBenchmark.resizeInt    10000  avgt   20   5304.983 ±  442.504  ns/op
    ToArrayBenchmark.resizeInt   100000  avgt   20  75084.243 ± 7344.678  ns/op
    ToArrayBenchmark.resizeObj        1  avgt   20     12.217 ±    0.829  ns/op
    ToArrayBenchmark.resizeObj      100  avgt   20     58.948 ±    4.710  ns/op
    ToArrayBenchmark.resizeObj     1000  avgt   20    652.498 ±   94.395  ns/op
    ToArrayBenchmark.resizeObj     5000  avgt   20   3567.842 ±  483.469  ns/op
    ToArrayBenchmark.resizeObj    10000  avgt   20   6478.753 ±  888.659  ns/op
    ToArrayBenchmark.resizeObj   100000  avgt   20  70551.699 ± 5263.672  ns/op
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ToArrayBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(20)
                .threads(1)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        for (int i = 0; i < n; i++) {
            objList.add(0);
            intList.add(0);
        }
    }

    @Benchmark
    public Object[] preSizeObj() {
        return objList.toArray(new Object[n]);
    }

    @Benchmark
    public Object[] resizeObj() {
        return objList.toArray(new Object[0]);
    }

    @Benchmark
    public Object[] preSizeInt() {
        return intList.toArray(new Object[n]);
    }

    @Benchmark
    public Object[] resizeInt() {
        return intList.toArray(new Object[0]);
    }
}
