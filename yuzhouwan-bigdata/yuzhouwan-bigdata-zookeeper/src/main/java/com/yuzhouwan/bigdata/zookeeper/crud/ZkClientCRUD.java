package com.yuzhouwan.bigdata.zookeeper.crud;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：ZkClient CRUD
 *
 * @author Benedict Jin
 * @since 2015/12/10 0010
 */
public class ZkClientCRUD {

    private static final String HOST = "127.0.0.1";
    private static final int CLIENT_PORT = 2181;

    private static final int TIME_OUT_MILLISECOND = 5000;

    private ZkClient zkClient;

    private ZkSerializer zkSerializer;

    public ZkClientCRUD() throws Exception {
        init();
    }

    private void init() throws Exception {
        zkClient = new ZkClient(HOST.concat(":" + CLIENT_PORT), TIME_OUT_MILLISECOND);

        zkSerializer = new ZkSerializer() {
            /**
             * TODO{Benedict Jin}: kryo
             */
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                return new byte[0];
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return null;
            }
        };
//        zkClient.setZkSerializer(zkSerializer);
    }

    public void create(String path, Object data, CreateMode createMode) {
        zkClient.create(path, data, createMode);
    }

    public String read(String path) {
        return zkClient.readData(path);
    }

    public void update(String path, Object data, int version) {
        zkClient.writeData(path, data, version);
    }

    public void delete(String path) {
        zkClient.delete(path);
    }

    public List<String> getChildren(String path) {
        return zkClient.getChildren(path);
    }

    public boolean exist(String path) {
        return zkClient.exists(path);
    }

    public static void main(String[] args) throws Exception {
        ZkClientCRUD zkClientCRUD = new ZkClientCRUD();
        String origin = "/origin";
        if (!zkClientCRUD.exist(origin))
            zkClientCRUD.create(origin, "", CreateMode.PERSISTENT);
        zkClientCRUD.create(origin.concat("/1"), "1", CreateMode.EPHEMERAL);
        zkClientCRUD.create(origin.concat("/2"), "2", CreateMode.EPHEMERAL);
        zkClientCRUD.create(origin.concat("/3"), "3", CreateMode.EPHEMERAL);
        zkClientCRUD.create(origin.concat("/4"), "4", CreateMode.EPHEMERAL);

        System.out.println(zkClientCRUD.read(origin.concat("/1")));
//        System.out.println(zkClientCRUD.read(origin.concat("/1,/2")));    // not support list
//        System.out.println(zkClientCRUD.read(origin.concat("/*")));       // not support pattern
    }
}
