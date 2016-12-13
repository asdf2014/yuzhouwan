package com.yuzhouwan.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Thread Utils
 *
 * @author Benedict Jin
 * @since 2016/12/9
 */
public class ThreadUtils {

    private static final Logger _log = LoggerFactory.getLogger(ThreadUtils.class);

    private static final Long minPeriod = Long.parseLong(PropUtils.getInstance().getProperty("thread.min.period.millisecond"));

    public static ExecutorService buildExecutorService(Integer jobThreadCorePoolSize,
                                                       Integer jobThreadMaximumPoolSize,
                                                       Integer jobThreadKeepAliveSecond,
                                                       Integer jobArrayBlockingQueueSize, String poolName, boolean isDaemon) {
        ExecutorService executorServiceMetrics;
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        if (!StrUtils.isEmpty(poolName)) {
            threadFactoryBuilder.setNameFormat("[".concat(poolName).concat("]-%d"));
        }
        ThreadFactory threadFactory = threadFactoryBuilder.setDaemon(isDaemon).build();
        if (jobThreadCorePoolSize == null || jobThreadMaximumPoolSize == null
                || jobThreadKeepAliveSecond == null || jobArrayBlockingQueueSize == null) {
            executorServiceMetrics = Executors.newCachedThreadPool(threadFactory);
        } else {
            try {
                executorServiceMetrics = new ThreadPoolExecutor(jobThreadCorePoolSize,
                        jobThreadMaximumPoolSize, jobThreadKeepAliveSecond, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(jobArrayBlockingQueueSize), threadFactory);
            } catch (Exception e) {
                _log.error(ExceptionUtils.errorInfo(e));
                executorServiceMetrics = Executors.newCachedThreadPool(threadFactory);
            }
        }
        return executorServiceMetrics;
    }


    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void wait4Period(long passedPeriod, long aimPeriod) {
        try {
            long sleep;
            if (passedPeriod < aimPeriod) {
                sleep = aimPeriod - passedPeriod;
                if (sleep < minPeriod)
                    sleep = minPeriod;
            } else {
                _log.warn("Thread:{}, Used Time: {}, Excepted Period Time: {}", Thread.currentThread().getName(),
                        passedPeriod, aimPeriod);
                sleep = minPeriod;
            }
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            _log.error(ExceptionUtils.errorInfo(e));
        }
    }
}
