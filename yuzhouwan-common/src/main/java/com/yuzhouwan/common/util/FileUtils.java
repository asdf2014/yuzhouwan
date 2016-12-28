package com.yuzhouwan.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: File Utils
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class FileUtils {

    public static byte[] readFile(String filename) throws IOException {
        long len;
        File file;
        byte[] data = new byte[(int) (len = (file = new File(filename)).length())];
        try (FileInputStream fin = new FileInputStream(file)) {
            int r;
            if ((r = fin.read(data)) != len)
                throw new IOException(String.format("Only read %d of %d for %s", r, len, file.getName()));
            return data;
        }
    }

    public static void writeFile(String filename, byte data[])
            throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            fileOutputStream.write(data);
        }
    }
}