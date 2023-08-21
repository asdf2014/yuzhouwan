package com.yuzhouwan.site.service.rpc.core;

import com.yuzhouwan.site.api.rpc.model.Call;
import com.yuzhouwan.site.api.rpc.service.Server;
import com.yuzhouwan.site.service.rpc.connection.Client;
import com.yuzhouwan.site.service.rpc.connection.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：RPC
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class RPC {

    private static final Logger LOGGER = LoggerFactory.getLogger(RPC.class);

    /**
     * @param clazz Provide an interface for the service, so that the server can check if the service is provided
     * @param host  The host of server
     * @param port  The port of server
     * @return The proxy object
     **/
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class<T> clazz, String host, int port) {
        final Client client = new Client(host, port);
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            Call call = new Call();
            call.setInterfaces(clazz);
            call.setMethodName(method.getName());
            call.setParams(args);
            call.setParameterTypes(method.getParameterTypes());
            client.invokeCall(call);
            return call.getResult();
        };
        return (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[]{clazz}, handler);
    }

    /**
     * GenericTest
     * RPCServer.
     */
    public static class RPCServer implements Server {

        // The key is the service, the value is the concrete implementation
        Map<String, Object> serviceEntry = new HashMap<>();
        boolean isRunning = false;
        private int port = 20222;

        @Override
        public void start() {
            this.isRunning = true;
            new Thread(new Listener(this)).start();
        }

        @Override
        public void stop() {
            this.isRunning = false;
        }

        // register service
        @Override
        public void register(Class<?> serviceInterface, Class<?> serviceImp) {
            if (!serviceEntry.containsKey(serviceInterface.getName())) {
                try {
                    serviceEntry.put(serviceInterface.getName(), serviceImp.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Call the specific implementation of the server
        @Override
        public void call(Call call) {
            String interfaceName = call.getInterfaces().getName();
            Object object = serviceEntry.get(interfaceName);
            if (object != null) {
                try {
                    Class<?> clazz = object.getClass();
                    Method method = clazz.getMethod(call.getMethodName(), call.getParameterTypes());
                    Object result = method.invoke(object, call.getParams());
                    call.setResult(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.warn("Not registered this interface: {}!", interfaceName);
            }
        }

        @Override
        public boolean isRunning() {
            return this.isRunning;
        }

        @Override
        public int getPort() {
            return this.port;
        }
    }
}
