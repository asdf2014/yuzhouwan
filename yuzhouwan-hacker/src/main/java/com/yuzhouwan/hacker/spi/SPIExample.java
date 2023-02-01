package com.yuzhouwan.hacker.spi;

import java.util.ServiceLoader;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：SPI Example
 *
 * @author Benedict Jin
 * @see <a href="https://yuzhouwan.com/posts/190413/">那些绕不过去的 Java 知识点</a>
 * @since 2020/3/11
 */
public class SPIExample {

    public static void main(String[] args) {
        for (IStore store : ServiceLoader.load(IStore.class)) {
            System.err.println("Loaded " + store.getClass().getSimpleName());
            store.record("yuzhouwan.com");
        }
    }
}
