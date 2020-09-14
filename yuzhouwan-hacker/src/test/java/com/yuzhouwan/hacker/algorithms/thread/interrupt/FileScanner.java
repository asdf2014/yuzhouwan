package com.yuzhouwan.hacker.algorithms.thread.interrupt;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.common.util.ExceptionUtils;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.atomic.LongAdder;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：File Scanner
 *
 * @author Benedict Jin
 * @since 2017/9/29
 */
public class FileScanner {

    private static void listFile(File f) throws InterruptedException {
        if (Thread.interrupted()) {
            System.out.println("FileScanner was interrupted!");
            throw new InterruptedException("FileScanner was interrupted!");
        }
        Thread.sleep(2);
        if (f == null) throw new IllegalArgumentException();
        if (f.isFile()) {
            System.out.println(f);
            return;
        }
        File[] files = f.listFiles();
        if (files == null)
            throw new RuntimeException(String.format("Cannot get the file list from %s!", f.getName()));
        for (File file : files) {
            listFile(file);
        }
    }

    @Test
    public void test() throws Exception {
        final Thread fileScanner = new Thread() {
            public void run() {
                try {
                    listFile(new File(DirUtils.PROJECT_BASE_PATH));
                } catch (InterruptedException e) {
                    ExceptionUtils.errorInfo(e);
                }
            }
        };
        final Thread interrupter = new Thread("Interrupter") {
            private LongAdder longAdder = new LongAdder();

            public void run() {
                while (true) {
                    int adder = longAdder.intValue();
                    System.out.println(adder);
                    if (adder >= 5)
                        if (!fileScanner.isInterrupted()) {
                            System.out.println("FileScanner is not be interrupted!");
                            fileScanner.interrupt();
                            System.out.println("FileScanner has been interrupted.");
                            return;
                        }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        ExceptionUtils.errorInfo(e);
                    }
                    longAdder.increment();
                }
            }
        };
        fileScanner.start();
        interrupter.start();
        boolean isInterrupted, isAlive = true;
        while (isAlive) {
            isInterrupted = fileScanner.isInterrupted();
            isAlive = fileScanner.isAlive();
            System.out.println(String.format("isInterrupted: %s, isAlive: %s", isInterrupted, isAlive));
            Thread.sleep(10);
        }
    }
}
