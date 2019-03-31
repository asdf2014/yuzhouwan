package com.yuzhouwan.bigdata.hadoop.io;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: IOUtils Tester
 *
 * @author Benedict Jin
 * @since 2016/8/3
 */
public class IOUtilsTest {

    /**
     * 在Linux系统之中，EOF根本不是一个字符，而是当系统读取到文件结尾，所返回的一个信号值（也就是-1）
     * http://www.ruanyifeng.com/blog/2011/11/eof.html
     * <p>
     * 描述：
     * 执行命令 hdfs dfs -appendToFile - hdfs://nn.example.com/hadoop/hadoopfile Reads the input from stdin的时候，Ctrl+C
     * <p>
     * 流程(终端 -> Kernel -> Hadoop -> 终端)：
     * Ctrl+C STD/IO -> SIGINT信号 --send--> 外壳 -> SIGINT信号 --send & kill--> 前台进程组(每个进程) --close--> stream
     * -> 释放行缓冲(非终端设备，默认为全缓冲) --call--> int fclose(File *fp) -> 0:success;EOF:error --success--> process exit
     * ---|Hadoop|---> CopyCommands.processArguments() -> IOUtils.copyBytes -> System.in.read(buffer)
     * --throw--> IOException: Stream closed --print--> 0:success;1:error
     * <p>
     * <p>
     * 参考：
     * 《深入理解计算机系统(2nd)》第8章 8.5小节
     * 《Unix环境高级编程(3rd)》第5章
     * <p>
     * [code]: https://github.com/c9n/hadoop/blob/master/hadoop-common-project/hadoop-common/src/main/java/org/apache/hadoop/fs/shell/CopyCommands.java#L346
     * [doc]:  https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html#appendToFile
     */
    private static final int DEFAULT_IO_LENGTH = 1024 * 1024;
    private static final String FILE_PATH = System.getProperty("user.dir").concat("/src/test/resources/io.copy");

    @Test
    public void test() throws Exception {
        File f = new File(FILE_PATH);
        assertTrue(f.delete());
        String msg = "yuzhouwan";

        System.setIn(new ReaderInputStream(new StringReader(msg)));
        IOUtils.copyBytes(System.in, new FileOutputStream(f), DEFAULT_IO_LENGTH);

        byte[] buff = new byte[DEFAULT_IO_LENGTH];
        int len = new FileInputStream(f).read(buff, 0, msg.getBytes().length);

        assertEquals(msg, new String(buff, 0, len));
    }

    @Test
    public void readTest() {
        try {
            System.in.close();
            System.out.println(System.in.read());
        } catch (IOException ioe) {
            assertEquals("Stream closed", ioe.getMessage());
        }
    }
}
