package com.yuzhouwan.hacker.debug;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Benchmark Debug
 *
 * @author Benedict Jin
 * @since 2023/12/25
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkDebug {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkDebug.class);

    /*
    Benchmark                              Mode  Cnt        Score         Error   Units
    BenchmarkDebug.debugArgsWithCheck     thrpt    5  4601203.764 ± 1829959.474  ops/ms
    BenchmarkDebug.debugArgsWithoutCheck  thrpt    5   466742.638 ±   15333.407  ops/ms
    BenchmarkDebug.debugWithCheck         thrpt    5  4174715.121 ± 1020722.679  ops/ms
    BenchmarkDebug.debugWithoutCheck      thrpt    5  3896071.922 ± 2435909.746  ops/ms
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
          .include(BenchmarkDebug.class.getSimpleName())
          .forks(1)
          .warmupIterations(5)
          .measurementIterations(5)
          .threads(10)
          .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void debugWithCheck() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("debugWithCheck");
        }
    }

    @Benchmark
    public void debugWithoutCheck() {
        LOGGER.debug("debugWithoutCheck");
    }

    @Benchmark
    public void debugArgsWithCheck() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("debugArgsWithCheck {} {} {}", "a", 2, 3.0D);
        }
    }

    @Benchmark
    public void debugArgsWithoutCheck() {
        LOGGER.debug("debugArgsWithoutCheck {} {} {}", "a", 2, 3.0D);
    }
}
