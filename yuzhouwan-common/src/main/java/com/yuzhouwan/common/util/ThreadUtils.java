package com.yuzhouwan.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Thread Utils
 *
 * @author Benedict Jin
 * @since 2016/12/9
 */
public final class ThreadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtils.class);
    private static long MIN_PERIOD_MILLISECOND;

    static {
        String minPeriodStr = PropUtils.getInstance().getProperty("thread.min.period.millisecond");
        if (StrUtils.isEmpty(minPeriodStr)) MIN_PERIOD_MILLISECOND = 0L;
        else MIN_PERIOD_MILLISECOND = Long.parseLong(minPeriodStr);
        if (MIN_PERIOD_MILLISECOND < 0) MIN_PERIOD_MILLISECOND = 0L;
    }

    private ThreadUtils() {
    }

    /**
     * CachedThreadPool.
     */
    public static ExecutorService buildExecutorService(String poolName) {
        return buildExecutorService(null, poolName, null);
    }

    /**
     * CachedThreadPool with isDaemon.
     */
    public static ExecutorService buildExecutorService(String poolName, Boolean isDaemon) {
        return buildExecutorService(null, poolName, isDaemon);
    }

    /**
     * FixedThreadPool.
     */
    public static ExecutorService buildExecutorService(Integer nThread, String poolName) {
        return buildExecutorService(nThread, poolName, null);
    }

    /**
     * FixedThreadPool with isDaemon.
     */
    public static ExecutorService buildExecutorService(Integer nThread, String poolName, Boolean isDaemon) {
        if (nThread != null && nThread >= 0)
            return Executors.newFixedThreadPool(nThread, buildThreadFactory(poolName, isDaemon));
        else return buildExecutorService(null, null, null, null, poolName, isDaemon);
    }

    /**
     * ThreadPoolExecutor with LinkedBlockingQueue.
     */
    public static ExecutorService buildExecutorService(Integer nThreadCore, Integer nThreadMax, Long ttlMillisecond,
                                                       String poolName, Boolean isDaemon) {
        if (nThreadCore != null && nThreadCore >= 0 && nThreadMax != null && nThreadMax >= 0
                && ttlMillisecond != null && ttlMillisecond >= 0)
            return new ThreadPoolExecutor(nThreadCore, nThreadMax, ttlMillisecond, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), buildThreadFactory(poolName, isDaemon));
        else return buildExecutorService(null, null, null, null, poolName, isDaemon);
    }

    /**
     * CachedThreadPool / (ThreadPoolExecutor with ArrayBlockingQueue).
     */
    public static ExecutorService buildExecutorService(Integer jobThreadCorePoolSize, Integer jobThreadMaximumPoolSize,
                                                       Long jobThreadKeepAliveSecond, Integer jobArrayBlockingQueueSize,
                                                       String poolName, Boolean isDaemon) {
        ThreadFactory threadFactory = buildThreadFactory(poolName, isDaemon);
        ExecutorService executorService;
        if (jobThreadCorePoolSize == null || jobThreadMaximumPoolSize == null
                || jobThreadKeepAliveSecond == null || jobArrayBlockingQueueSize == null)
            executorService = Executors.newCachedThreadPool(threadFactory);
        else try {
            executorService = new ThreadPoolExecutor(jobThreadCorePoolSize, jobThreadMaximumPoolSize,
                    jobThreadKeepAliveSecond, TimeUnit.SECONDS,
                    // jdk7: new ArrayBlockingQueue<Runnable>
                    new ArrayBlockingQueue<>(jobArrayBlockingQueueSize), threadFactory);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
            executorService = Executors.newCachedThreadPool(threadFactory);
        }
        return executorService;
    }

    private static ThreadFactory buildThreadFactory(String poolName, Boolean isDaemon) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        if (!StrUtils.isEmpty(poolName)) threadFactoryBuilder.setNameFormat("[".concat(poolName).concat("]-%d"));
        if (isDaemon != null) threadFactoryBuilder.setDaemon(isDaemon);
        return threadFactoryBuilder.build();
    }

    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int availableProcessors4IO() {
        return availableProcessors() * 2;
    }

    public static int availableProcessors4Computing() {
        return availableProcessors() + 1;
    }

    public static void wait4Period(long passedPeriod, long aimPeriod) {
        wait4Period(passedPeriod, aimPeriod, MIN_PERIOD_MILLISECOND);
    }

    public static void wait4Period(long passedPeriod, long aimPeriod, long minPeriod) {
        try {
            long sleep;
            if (passedPeriod < aimPeriod) {
                if ((sleep = aimPeriod - passedPeriod) < minPeriod) sleep = minPeriod;
            } else {
                LOGGER.warn("Thread:{}, Used Time: {}, Excepted Period Time: {}",
                        Thread.currentThread().getName(), passedPeriod, aimPeriod);
                sleep = minPeriod;
            }
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            LOGGER.error(ExceptionUtils.errorInfo(e));
        }
    }
}
