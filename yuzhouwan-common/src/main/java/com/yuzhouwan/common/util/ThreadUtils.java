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

    private static long MIN_PERIOD;

    static {
        String minPeriodStr;
        if (StrUtils.isEmpty(minPeriodStr = PropUtils.getInstance().getProperty("thread.min.period.millisecond")))
            MIN_PERIOD = 0L;
        else
            MIN_PERIOD = Long.parseLong(minPeriodStr);
    }

    public static ExecutorService buildExecutorService(Integer jobThreadCorePoolSize,
                                                       Integer jobThreadMaximumPoolSize,
                                                       Integer jobThreadKeepAliveSecond,
                                                       Integer jobArrayBlockingQueueSize, String poolName, boolean isDaemon) {
        ExecutorService executorService;
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        if (!StrUtils.isEmpty(poolName)) threadFactoryBuilder.setNameFormat("[".concat(poolName).concat("]-%d"));
        ThreadFactory threadFactory = threadFactoryBuilder.setDaemon(isDaemon).build();
        if (jobThreadCorePoolSize == null || jobThreadMaximumPoolSize == null
                || jobThreadKeepAliveSecond == null || jobArrayBlockingQueueSize == null) {
            executorService = Executors.newCachedThreadPool(threadFactory);
        } else {
            try {
                executorService = new ThreadPoolExecutor(jobThreadCorePoolSize, jobThreadMaximumPoolSize,
                        jobThreadKeepAliveSecond, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(jobArrayBlockingQueueSize), threadFactory);
            } catch (Exception e) {
                _log.error(ExceptionUtils.errorInfo(e));
                executorService = Executors.newCachedThreadPool(threadFactory);
            }
        }
        return executorService;
    }


    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void wait4Period(long passedPeriod, long aimPeriod) {
        try {
            long sleep;
            if (passedPeriod < aimPeriod) {
                sleep = aimPeriod - passedPeriod;
                if (sleep < MIN_PERIOD) sleep = MIN_PERIOD;
            } else {
                _log.warn("Thread:{}, Used Time: {}, Excepted Period Time: {}", Thread.currentThread().getName(),
                        passedPeriod, aimPeriod);
                sleep = MIN_PERIOD;
            }
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            _log.error(ExceptionUtils.errorInfo(e));
        }
    }
}
