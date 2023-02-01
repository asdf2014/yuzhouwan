package com.yuzhouwan.site.service.rpc.connection;

import com.yuzhouwan.site.api.rpc.model.Call;
import com.yuzhouwan.site.api.rpc.service.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Listener
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class Listener implements Runnable {

    private static final Logger _log = LoggerFactory.getLogger(Listener.class);

    private Server server;

    public Listener(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        // 首先定义服务器监听socket
        ServerSocket serversocket;
        try {
            serversocket = new ServerSocket(server.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (server.isRunning()) {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                Socket clientSocket = serversocket.accept();
                // 获取客户端的请求
                ois = new ObjectInputStream(clientSocket.getInputStream());
                Call call = (Call) ois.readObject();  // lgtm [java/unsafe-deserialization]
                // 服务器处理
                server.call(call);

                // 再将结果返回给客户端
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(call);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                release(oos, ois);
            }
        }
    }

    private void release(ObjectOutputStream oos, ObjectInputStream ois) {
        try {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    _log.error("Release resource failed: {}!", e.getMessage());
                }
            }
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    _log.error("Release resource failed: {}!", e.getMessage());
                }
            }
        }
    }
}
