package com.yuzhouwan.hacker.jvm.hook;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：ShutdownHook Example
 *
 * @author Benedict Jin
 * @since 2019/8/7
 */
public class ShutdownHookExample {

    /**
     * registering
     * registered
     * exit
     * shutdown2...
     * shutdown3...
     * shutdown1...
     *
     * @see <a href="https://yuzhouwan.com/posts/190413/#Shutdown-Hook">Shutdown Hook</a>
     */
    public static void main(String[] args) {
        System.out.println("registering");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("shutdown1...")));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("shutdown2...")));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("shutdown3...")));
        System.out.println("registered");
        System.out.println("exit");
        System.exit(0);
    }
}
