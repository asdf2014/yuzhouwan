package com.yuzhouwan.site.api.rpc.service;

import com.yuzhouwan.site.api.rpc.model.Call;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Server
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public interface Server {

    void start();

    void stop();

    void register(Class serviceInterface, Class serviceImp);

    void call(Call call);

    boolean isRunning();

    int getPort();
}
