package com.yuzhouwan.hacker.algorithms.collection;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Copyright @ 2018 yuzhouwan.com
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

    @Param({"1", "100", "1000", "5000", "10000", "100000"})
    private int n;

    private final List<Object> list = new ArrayList<>();

    @Setup
    public void populateList() {
        for (int i = 0; i < n; i++) {
            list.add(0);
        }
    }

    @Benchmark
    public Object[] preSize() {
        return list.toArray(new Object[n]);
    }

    @Benchmark
    public Object[] resize() {
        return list.toArray(new Object[0]);
    }

    /*
    Integer List:
    Benchmark                    (n)  Mode  Cnt       Score        Error  Units
    ToArrayBenchmark.preSize       1  avgt    3      41.552 ±    108.030  ns/op
    ToArrayBenchmark.preSize     100  avgt    3     216.449 ±    799.501  ns/op
    ToArrayBenchmark.preSize    1000  avgt    3    2087.965 ±   6027.778  ns/op
    ToArrayBenchmark.preSize    5000  avgt    3    9098.358 ±  14603.493  ns/op
    ToArrayBenchmark.preSize   10000  avgt    3   24204.199 ± 121468.232  ns/op
    ToArrayBenchmark.preSize  100000  avgt    3  188183.618 ± 369455.090  ns/op
    ToArrayBenchmark.resize        1  avgt    3      18.987 ±     36.449  ns/op
    ToArrayBenchmark.resize      100  avgt    3     265.549 ±   1125.008  ns/op
    ToArrayBenchmark.resize     1000  avgt    3    1560.713 ±   2922.186  ns/op
    ToArrayBenchmark.resize     5000  avgt    3    7804.810 ±   8333.390  ns/op
    ToArrayBenchmark.resize    10000  avgt    3   24791.026 ±  78459.936  ns/op
    ToArrayBenchmark.resize   100000  avgt    3  158891.642 ±  56055.895  ns/op

    Object List:
    Benchmark                    (n)  Mode  Cnt      Score       Error  Units
    ToArrayBenchmark.preSize       1  avgt    3     36.306 ±    96.612  ns/op
    ToArrayBenchmark.preSize     100  avgt    3     52.372 ±    84.159  ns/op
    ToArrayBenchmark.preSize    1000  avgt    3    449.807 ±   215.692  ns/op
    ToArrayBenchmark.preSize    5000  avgt    3   2080.172 ±  2003.726  ns/op
    ToArrayBenchmark.preSize   10000  avgt    3   4657.937 ±  8432.624  ns/op
    ToArrayBenchmark.preSize  100000  avgt    3  51980.829 ± 46920.314  ns/op
    ToArrayBenchmark.resize        1  avgt    3     16.747 ±    85.131  ns/op
    ToArrayBenchmark.resize      100  avgt    3     43.803 ±    28.704  ns/op
    ToArrayBenchmark.resize     1000  avgt    3    404.681 ±   132.986  ns/op
    ToArrayBenchmark.resize     5000  avgt    3   1972.649 ±   174.691  ns/op
    ToArrayBenchmark.resize    10000  avgt    3   4021.440 ±  1114.212  ns/op
    ToArrayBenchmark.resize   100000  avgt    3  44204.167 ± 76714.850  ns/op
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ToArrayBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .threads(1)
                .build();
        new Runner(opt).run();
    }
}
