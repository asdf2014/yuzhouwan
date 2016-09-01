package com.yuzhouwan.site.rpc.core;

import com.yuzhouwan.site.rpc.connection.Server;
import com.yuzhouwan.site.rpc.service.IRPCService;
import com.yuzhouwan.site.rpc.service.RPCServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: RPC Tester
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class RPCTest {

    @Test
    public void rpc() throws Exception {

        Server server = new RPC.RPCServer();
        server.register(IRPCService.class, RPCServiceImpl.class);
        server.start();

        IRPCService testServiceImpl = RPC.getProxy(IRPCService.class, "localhost", server.getPort());
        assertEquals("Hello, Benedict!", testServiceImpl.printHello("Benedict"));
        server.stop();  // System.exit(0);
    }
}
