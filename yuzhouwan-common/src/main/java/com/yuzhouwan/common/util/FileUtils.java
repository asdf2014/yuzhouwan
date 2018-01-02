package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: File Utils
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public final class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static final int RETRY_DELETE_DEFAULT_TIMES = 3;
    public static final int RETRY_DELETE_DEFAULT_INTERVAL_MS = 100;

    private FileUtils() {
    }

    public static byte[] readFile(String filename) throws IOException {
        if (StrUtils.isEmpty(filename)) return null;
        File file = new File(filename);
        if (!checkExist(file)) return null;
        long fileLen = file.length();
        byte[] data = new byte[(int) fileLen];
        try (FileInputStream fin = new FileInputStream(file)) {
            int readLen = fin.read(data);
            if (readLen != fileLen)
                throw new IOException(String.format("Only read %d of %d for %s", readLen, fileLen, file.getName()));
            return data;
        }
    }

    public static void writeFile(String filename, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }

    public static boolean checkExist(File f) {
        return f != null && f.exists();
    }

    public static void retryDelete(File file) {
        retryDelete(file, RETRY_DELETE_DEFAULT_TIMES, RETRY_DELETE_DEFAULT_INTERVAL_MS);
    }

    public static void retryDelete(File file, int count) {
        retryDelete(file, count, RETRY_DELETE_DEFAULT_INTERVAL_MS);
    }

    public static void retryDelete(File file, int count, long millisecond) {
        int copyCount = count;
        while (checkExist(file) && count > 0) {
            LOG.debug("File Delete Try Count: {}", copyCount - count + 1);
            if (file.delete()) {
                LOG.debug("File Deleted!");
                return;
            }
            count--;
            try {
                Thread.sleep(millisecond);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
