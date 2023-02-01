package com.yuzhouwan.bigdata.zookeeper.auth;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Auth Digest Example
 *
 * @author Benedict Jin
 * @since 2017/8/22
 */
public class AuthDigestExample {

    private static final String HOST = "localhost";
    private static final int CLIENT_PORT = 2181;
    private static final int TIME_OUT_MILLISECOND = 5000;
    private static final String AUTH_PATH = "/auth_test";
    private static final String AUTH_PATH_CHILD = AUTH_PATH.concat("/child");
    private static final byte[] bytes = AUTH_PATH.getBytes();

    private ZooKeeper zoo, zooNoAuth;

    @Before
    public void init() throws Exception {
        zoo = new ZooKeeper(HOST.concat(":" + CLIENT_PORT), TIME_OUT_MILLISECOND, null);
        zoo.addAuthInfo("digest", "yuzhouwan:com".getBytes());
        zooNoAuth = new ZooKeeper(HOST.concat(":" + CLIENT_PORT), TIME_OUT_MILLISECOND, null);
    }

    @Test
    public void digestAcl() throws Exception {
        /*
        $ zkCli.sh -server localhost:2181
        [zk: localhost:2181(CONNECTED) 5] ls /
          [leader, auth_test, election, zookeeper, benchmark, origin]

        [zk: localhost:2181(CONNECTED) 6] ls /auth_test
          Authentication is not valid : /auth_test

        [zk: localhost:2181(CONNECTED) 7] get /auth_test
          Authentication is not valid : /auth_test

        [zk: localhost:2181(CONNECTED) 8] getAcl /auth_test
          'digest,'yuzhouwan:h/j+/wDlblTtA48jnbq8snP1glA=
          : cdrwa

        [zk: localhost:2181(CONNECTED) 9] addauth digest yuzhouwan:true

        [zk: localhost:2181(CONNECTED) 10] get /auth_test
          /auth_test
          cZxid = 0x10000c31e
          ctime = Tue Aug 22 15:26:27 CST 2017
          mZxid = 0x10000c31e
          mtime = Tue Aug 22 15:26:27 CST 2017
          pZxid = 0x10000c31e
          cversion = 0
          dataVersion = 0
          aclVersion = 0
          ephemeralOwner = 0x0
          dataLength = 10
          numChildren = 0
          */
        if (zoo.exists(AUTH_PATH_CHILD, null) != null) zoo.delete(AUTH_PATH_CHILD, -1);
        if (zoo.exists(AUTH_PATH, null) != null) zoo.delete(AUTH_PATH, -1);
        zoo.create(AUTH_PATH, bytes, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        try {
            zooNoAuth.create(AUTH_PATH_CHILD, bytes, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        } catch (KeeperException.InvalidACLException e) {
            assertEquals("KeeperErrorCode = InvalidACL for /auth_test/child", e.getMessage());
        }
        zoo.create(AUTH_PATH_CHILD, bytes, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        try {
            zooNoAuth.delete(AUTH_PATH_CHILD, -1);
        } catch (KeeperException.NoAuthException e) {
            assertEquals("KeeperErrorCode = NoAuth for /auth_test/child", e.getMessage());
        }
        assertEquals(AUTH_PATH, new String(zoo.getData(AUTH_PATH, false, null)));
    }

    @After
    public void close() throws Exception {
        zoo.delete(AUTH_PATH_CHILD, -1);
        zoo.delete(AUTH_PATH, -1);
        zoo.close();
    }
}
