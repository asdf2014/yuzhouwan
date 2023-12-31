package com.yuzhouwan.hacker.str;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;


/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：String Equals Benchmark
 *
 * @author Benedict Jin
 * @since 2018/8/1
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class StringEqualsBenchmark {

    private String blankStr;
    private String nonBlankStr;

    /*
    Benchmark                                   Mode  Cnt  Score   Error  Units
    StringEqualsBenchmark.equalsBlankString     avgt    8  0.977 ± 0.207  ns/op
    StringEqualsBenchmark.equalsNonBlankString  avgt    8  0.946 ± 0.024  ns/op
    StringEqualsBenchmark.sizeBlankString       avgt    8  1.186 ± 0.041  ns/op
    StringEqualsBenchmark.sizeNonBlankString    avgt    8  1.159 ± 0.044  ns/op
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(StringEqualsBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(8)
                .threads(2)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void set() {
        Object blankObj = "";
        blankStr = blankObj.toString();
        Object nonBlankObj = "s";
        nonBlankStr = nonBlankObj.toString();
    }

    @Benchmark
    public void equalsBlankString() {
        boolean b = "".equals(blankStr);
    }

    @Benchmark
    public void equalsNonBlankString() {
        boolean b = "".equals(nonBlankStr);
    }

    @Benchmark
    public void sizeBlankString() {
        boolean b = blankStr.length() == 0;
    }

    @Benchmark
    public void sizeNonBlankString() {
        boolean b = nonBlankStr.length() == 0;
    }
}
