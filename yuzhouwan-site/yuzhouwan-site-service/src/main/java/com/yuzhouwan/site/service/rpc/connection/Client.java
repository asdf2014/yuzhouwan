package com.yuzhouwan.site.service.rpc.connection;

import com.yuzhouwan.site.api.rpc.model.Call;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Client
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class Client {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void initClient() throws IOException {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public void invokeCall(Call call) throws IOException, ClassNotFoundException {
        initClient();
        oos.writeObject(call);
        oos.flush();
        //waiting server to send result...
        ois = new ObjectInputStream(socket.getInputStream());
        Call resultCall = (Call) ois.readObject();  // lgtm [java/unsafe-deserialization]
        call.setResult(resultCall.getResult());
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
