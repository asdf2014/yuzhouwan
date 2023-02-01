package com.yuzhouwan.hacker.jvm.heap;

import java.nio.ByteBuffer;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Off-Heap Example
 *
 * @author Benedict Jin
 * @since 2018/6/15
 */
public class OffHeapExample {

    /*
    -XX:MaxDirectMemorySize=64M 可以控制堆外内存大小，默认在 VM 静态变量 directMemory 为 64M

    maxDirectMemory: 67108864
    isDirect: true
     */
    public static void main(String[] args) throws InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 64);
        Thread.sleep(200);
        boolean isDirect = buffer.isDirect();
        System.out.println("isDirect: " + isDirect);
        buffer.clear();
        Thread.sleep(200);
        System.exit(0);
    }
}
