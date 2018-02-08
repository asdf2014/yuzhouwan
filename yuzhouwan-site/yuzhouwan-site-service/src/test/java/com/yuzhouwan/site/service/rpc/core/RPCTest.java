package com.yuzhouwan.site.service.rpc.core;

import com.yuzhouwan.site.api.rpc.service.IRPCService;
import com.yuzhouwan.site.api.rpc.service.Server;
import com.yuzhouwan.site.service.rpc.service.RPCServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: RPC Tester
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class RPCTest {

    @Test
    public void rpc() throws Exception {
        Server server = null;
        try {
            server = new RPC.RPCServer();
            server.register(IRPCService.class, RPCServiceImpl.class);
            server.start();

            int count = 0, max = 20;
            while (!server.isRunning() && count < max) {
                Thread.sleep(5);
                count++;
            }
            IRPCService testServiceImpl = RPC.getProxy(IRPCService.class, "localhost", server.getPort());
            assertEquals("Hello, Benedict!", testServiceImpl.printHello("Benedict"));
        } finally {
            if (server != null) server.stop();  // System.exit(0);
        }
    }
}
