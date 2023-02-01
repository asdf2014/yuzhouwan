package com.yuzhouwan.hacker.io;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：IO File Copy
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/190413/">那些绕不过去的 Java 知识点</a>
 * @since 2020/4/25
 */
public class IOFileCopy {

    private static final String SOURCE = System.getProperty("user.dir") + "/io_src.txt";
    private static final String DEST = System.getProperty("user.dir") + "/io_dest.txt";

    public static void main(String[] args) throws Exception {
        final String cont = RandomStringUtils.randomAlphanumeric(8 * 1024 * 1024);
        try (final FileOutputStream out = new FileOutputStream(SOURCE)) {
            out.write(cont.getBytes());
        }
        try (final FileInputStream in = new FileInputStream(SOURCE);
             final FileOutputStream out = new FileOutputStream(DEST)) {
            long start = System.currentTimeMillis();
            byte[] buf = new byte[1024];
            while (in.read(buf) >= 0) {
                out.write(buf);
            }
            out.flush();
            System.out.println("Cost: " + (System.currentTimeMillis() - start) + " ms");
        } finally {
            Files.deleteIfExists(Paths.get(SOURCE));
            Files.deleteIfExists(Paths.get(DEST));
        }
    }
}
