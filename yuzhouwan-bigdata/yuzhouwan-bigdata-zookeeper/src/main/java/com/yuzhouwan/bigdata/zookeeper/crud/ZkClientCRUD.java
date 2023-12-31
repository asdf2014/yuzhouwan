package com.yuzhouwan.bigdata.zookeeper.crud;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Copyright @ 2024 yuzhouwan.com
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

    public ZkClientCRUD() {
        init();
    }

    public static void main(String[] args) {
        testRmrHugeChildZnodesList();
    }

    private static void testRmrHugeChildZnodesList() {
        ZkClientCRUD zkClientCRUD = new ZkClientCRUD();
        String rmr = "/rmr";
        if (!zkClientCRUD.exist(rmr))
            zkClientCRUD.create(rmr, "", CreateMode.PERSISTENT);
        int len = 1_0001;
        String child;
        long beginCreate = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            child = rmr.concat("/") + i;
            if (i % 100 == 0) System.out.println("Creating " + child + " ...");
            /*if (!zkClientCRUD.exist(child)) */
            zkClientCRUD.create(child, "" + i, CreateMode.PERSISTENT);
            if (i % 100 == 0) System.out.println("Created " + child + " .");
        }
        long endCreate = System.currentTimeMillis();
        System.out.println("Deleting /rmr ...");
        long beginDelete = System.currentTimeMillis();
        zkClientCRUD.rmr(rmr);
        long endDelete = System.currentTimeMillis();
        if (zkClientCRUD.exist(rmr)) System.out.println("Delete /rmr failed!");
        else System.out.println("Deleted /rmr .");
        System.out.printf("Create used %s ms, Delete used %s ms, Size %s.%n",
                endCreate - beginCreate, endDelete - beginDelete, len - 1);
        System.out.println("Done.");
    }

    private static void testWatch() {
        ZkClientCRUD zkClientCRUD = new ZkClientCRUD();
        String origin = "/origin";
        if (!zkClientCRUD.exist(origin))
            zkClientCRUD.create(origin, "", CreateMode.PERSISTENT);
        int len = 4;
        String[] paths = new String[len];
        String path;
        for (int i = 0; i < len; i++) {
            path = origin.concat("/" + i);
            paths[i] = path;
            if (!zkClientCRUD.exist(path)) zkClientCRUD.create(path, "" + i, CreateMode.PERSISTENT);
        }

        for (String s : paths) {
            zkClientCRUD.watch(s);
            System.out.println(zkClientCRUD.read(s));
        }
//        System.out.println(zkClientCRUD.read(origin.concat("/1,/2")));    // not support list
//        System.out.println(zkClientCRUD.read(origin.concat("/*")));       // not support pattern

        for (String p : paths) zkClientCRUD.delete(p);
        System.out.println("Done.");
    }

    private void init() {
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

    public void rmr(String path) {
        zkClient.deleteRecursive(path);
    }

    public List<String> getChildren(String path) {
        return zkClient.getChildren(path);
    }

    public boolean exist(String path) {
        return zkClient.exists(path);
    }

    public void watch(String path) {
        zkClient.watchForData(path);
    }
}
