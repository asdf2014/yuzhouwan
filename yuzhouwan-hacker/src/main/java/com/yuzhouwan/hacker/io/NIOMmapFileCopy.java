package com.yuzhouwan.hacker.io;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：NIO Mmap File Copy
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/190413/">那些绕不过去的 Java 知识点</a>
 * @since 2020/4/25
 */
public class NIOMmapFileCopy {

    private static final String SOURCE = System.getProperty("user.dir") + "/nio_mmap_src.txt";
    private static final String DEST = System.getProperty("user.dir") + "/nio_mmap_dest.txt";

    public static void main(String[] args) throws Exception {
        final String cont = RandomStringUtils.randomAlphanumeric(8 * 1024 * 1024);
        try (final FileChannel src = new RandomAccessFile(SOURCE, "rw").getChannel();
             final FileChannel dest = new RandomAccessFile(DEST, "rw").getChannel()) {
            src.write(ByteBuffer.wrap(cont.getBytes()));
            long start = System.currentTimeMillis();
            final MappedByteBuffer mmap = dest.map(FileChannel.MapMode.READ_WRITE, 0, src.size());
            src.write(mmap);
            mmap.flip();
            System.out.println("Cost: " + (System.currentTimeMillis() - start) + " ms");
        } finally {
            Files.deleteIfExists(Paths.get(SOURCE));
            Files.deleteIfExists(Paths.get(DEST));
        }
    }
}
