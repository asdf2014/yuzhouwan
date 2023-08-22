package com.yuzhouwan.hacker.algorithms.thread.loop;

import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：thread
 *
 * @author Benedict Jin
 * @since 2017/03/02
 */
public class ScheduledExecutorServiceExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledExecutorServiceExample.class);

  private static final Random r = new Random();

  public static void main(String[] args) {
    int len = 25;
    final int[] count = new int[len];
    ScheduledExecutorService executorService
        = Executors.newScheduledThreadPool(ThreadUtils.availableProcessors4Computing());

    LOGGER.info("Start...");
    for (int i = 0; i < len; i++) {
      int finalI = i;
      executorService.scheduleAtFixedRate(() -> {
            long random = r.nextInt(3000);
            LOGGER.info("Task{}: {}, Count: {}, Random: {}",
                finalI, Thread.currentThread(), count[finalI]++, random);
            try {
              Thread.sleep(random);
            } catch (InterruptedException e) {
              LOGGER.error(ExceptionUtils.errorInfo(e));
            }
          },
          1000, 1000, TimeUnit.MILLISECONDS);
    }
  }
}
