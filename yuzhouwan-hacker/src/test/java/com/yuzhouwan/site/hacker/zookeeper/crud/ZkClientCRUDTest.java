package com.yuzhouwan.site.hacker.zookeeper.crud;

import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：ZkClientCRUD Tester
 *
 * @author Benedict Jin
 * @since 2015/12/10 0010
 */
public class ZkClientCRUDTest {

    private static final String PATH = "/yuzhouwan";

    private ZkClientCRUD zkClientCRUD;

    @Before
    public void before() throws Exception {
        zkClientCRUD = new ZkClientCRUD();
    }

    @After
    public void after() throws Exception {
        zkClientCRUD = null;
    }

    @Test
    public void testExist() throws Exception {
        zkClientCRUD.create(PATH, "123", CreateMode.PERSISTENT);
        assertEquals(true, zkClientCRUD.exist(PATH));

        String subPath = PATH.concat("/asdf");
        zkClientCRUD.create(subPath, new Integer(123), CreateMode.PERSISTENT);
        assertEquals(true, zkClientCRUD.exist(subPath));

        List<String> children = zkClientCRUD.getChildren(PATH);
        assertEquals("asdf", children.size() == 0 ? "" : children.get(0));

        zkClientCRUD.update(PATH, "123", -1);
        assertEquals("123", zkClientCRUD.read(PATH));

        zkClientCRUD.delete(subPath);
        assertEquals(false, zkClientCRUD.exist(subPath));
    }

}
