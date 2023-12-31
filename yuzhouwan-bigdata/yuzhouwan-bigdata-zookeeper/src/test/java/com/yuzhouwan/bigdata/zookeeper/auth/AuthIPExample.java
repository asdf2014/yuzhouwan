package com.yuzhouwan.bigdata.zookeeper.auth;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Auth IP Example
 *
 * @author Benedict Jin
 * @since 2017/8/22
 */
public class AuthIPExample {

    private static final String HOST = "localhost";
    private static final int CLIENT_PORT = 2181;
    private static final int TIME_OUT_MILLISECOND = 5000;
    private static final String IP = "ip";
    private static final String IP_PATH = "/".concat(IP);
    private static final String IP_PATH_NO_AUTH = "/".concat(IP).concat("_no_auth");

    private ZooKeeper zoo;
    private List<ACL> acls, aclsNoAuth;

    @Before
    public void init() throws Exception {
        zoo = new ZooKeeper(HOST.concat(":" + CLIENT_PORT), TIME_OUT_MILLISECOND, null);
        acls = new ArrayList<>();
        acls.add(new ACL(ZooDefs.Perms.ALL, new Id(IP, "10.24.40.178")));
        acls.add(new ACL(ZooDefs.Perms.ALL, new Id(IP, "127.0.0.1")));
        aclsNoAuth = new ArrayList<>();
        aclsNoAuth.add(new ACL(ZooDefs.Perms.ALL, new Id(IP, "127.0.0.1")));
    }

    @Test
    public void ipAcl() throws Exception {
        /*
        $ zkCli.sh -server localhost:2181
        [zk: localhost:2181(CONNECTED) 16] ls /
        [leader, election, zookeeper, origin, ip, auth_test, benchmark]

        [zk: localhost:2181(CONNECTED) 17] ls /ip
        Authentication is not valid : /ip

        [zk: localhost:2181(CONNECTED) 18] getAcl /ip
          'ip,'10.24.40.178
          : cdrwa
          'ip,'127.0.0.1
          : cdrwa


        $ zkCli.sh -server 127.0.0.1:2181
        [zk: 127.0.0.1:2181(CONNECTED) 1] ls /ip
          []

        [zk: 127.0.0.1:2181(CONNECTED) 2] get /ip
          ip
          cZxid = 0x10000c43b
          ctime = Tue Aug 22 16:50:37 CST 2017
          mZxid = 0x10000c43b
          mtime = Tue Aug 22 16:50:37 CST 2017
          pZxid = 0x10000c43b
          cversion = 0
          dataVersion = 0
          aclVersion = 0
          ephemeralOwner = 0x0
          dataLength = 2
          numChildren = 0
         */
        if (zoo.exists(IP_PATH, null) != null) zoo.delete(IP_PATH, -1);
        if (zoo.exists(IP_PATH_NO_AUTH, null) != null) zoo.delete(IP_PATH_NO_AUTH, -1);
        zoo.create(IP_PATH, IP.getBytes(), acls, CreateMode.PERSISTENT);
        assertEquals(IP, new String(zoo.getData(IP_PATH, false, null)));
        zoo.create(IP_PATH_NO_AUTH, IP.getBytes(), aclsNoAuth, CreateMode.PERSISTENT);
        try {
            zoo.getData(IP_PATH_NO_AUTH, false, null);
        } catch (KeeperException.NoAuthException e) {
            assertEquals("KeeperErrorCode = NoAuth for ".concat(IP_PATH_NO_AUTH), e.getMessage());
        }
    }

    @After
    public void close() throws Exception {
        if (zoo.exists(IP_PATH, null) != null) zoo.delete(IP_PATH, -1);
        if (zoo.exists(IP_PATH_NO_AUTH, null) != null) zoo.delete(IP_PATH_NO_AUTH, -1);
        zoo.close();
    }
}
