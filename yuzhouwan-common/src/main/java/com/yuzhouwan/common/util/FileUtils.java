package com.yuzhouwan.common.util;

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
}