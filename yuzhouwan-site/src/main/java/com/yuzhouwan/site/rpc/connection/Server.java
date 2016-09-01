package com.yuzhouwan.site.rpc.connection;


import com.yuzhouwan.site.rpc.protocol.Call;

/**
 * Copyright @ 2016 yuzhouwan.com
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
