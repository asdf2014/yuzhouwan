package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: File Utils
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class FileUtils {

    private static final Logger _log = LoggerFactory.getLogger(FileUtils.class);

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

    public static void writeFile(String filename, byte data[]) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }

    public static boolean checkExist(File f) {
        return f != null && f.exists();
    }

    public static void retryDelete(File file) {
        retryDelete(file, 3, 100);
    }

    public static void retryDelete(File file, int count) {
        retryDelete(file, count, 100);
    }

    public static void retryDelete(File file, int count, long millisecond) {
        int copyCount = count;
        while (checkExist(file) && count > 0) {
            _log.debug("File Delete Try Count: {}", copyCount - count + 1);
            if (file.delete()) {
                _log.debug("File Deleted!");
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