package com.yuzhouwan.site.service.rpc.service;

import com.yuzhouwan.site.api.rpc.service.IRPCService;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：RPCServiceImpl
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class RPCServiceImpl implements IRPCService {

    @Override
    public String printHello(String name) {
        return String.format("Hello, %s!", name);
    }
}
