package com.yuzhouwan.site.service.nio;

import org.junit.Test;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: NIO Server Tester
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class NIOServerTest {

    @Test
    public void server() throws Exception {

        new NIOServer().startServer();
        new Thread(new NIOClient()).start();

        Thread.sleep(20);
    }
}
