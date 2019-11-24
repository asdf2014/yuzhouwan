package com.yuzhouwan.common.dir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: 监控线程
 *
 * @author Benedict Jin
 * @since 2016/4/7
 */
public class WatchRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(WatchRunnable.class);

    private IDirUtils dealProcessor;
    private Long waitTime;
    private WatchService watchService;

    /**
     * Controller for thread stops safely.
     */
    private boolean isRunning = true;

    WatchRunnable(WatchService watchService, IDirUtils dealProcessor, Long waitTime) {
        this.dealProcessor = dealProcessor;
        this.waitTime = waitTime;
        this.watchService = watchService;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        WatchKey key = null;
        try {
            key = watchService.take();
        } catch (InterruptedException e) {
            LOG.error("WatchService is error!", e);
        }
        if (key == null) return;
        IDirUtils dirUtil = dealProcessor == null ? new DirUtils() : dealProcessor;
        while (isRunning) {
            if (waitTime != null && waitTime > 0)
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOG.error("Thread sleep error!", e);
                }
            if (!key.reset()) break;
            key.pollEvents().forEach(dirUtil::dealWithEvent);
        }
    }
}
