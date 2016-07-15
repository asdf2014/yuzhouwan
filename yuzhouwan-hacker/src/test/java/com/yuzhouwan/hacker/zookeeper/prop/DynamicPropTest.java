package com.yuzhouwan.hacker.zookeeper.prop;

import com.yuzhouwan.common.util.DirUtils;
import org.junit.Test;

import java.io.File;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.hacker.zookeeper.prop
 *
 * @author Benedict Jin
 * @since 2016/7/14
 */
public class DynamicPropTest {

    private static final String PROP_PATH = DirUtils.RESOURCES_PATH.concat("prop/");

    @Test
    public void dynamic() throws Exception {

        DynamicProp dynamicProp = new DynamicProp(PROP_PATH);
        dynamicProp.startWatch();

        DirUtils.makeSureExist(PROP_PATH.concat("a"), false);
        DirUtils.makeSureExist(PROP_PATH.concat("b/"), false);
        DirUtils.makeSureExist(PROP_PATH.concat("c.txt"), true);

        new File(PROP_PATH.concat("a")).delete();
        new File(PROP_PATH.concat("b/")).delete();
        new File(PROP_PATH.concat("c.txt")).delete();

        new File(PROP_PATH).deleteOnExit();

        Thread.sleep(500);
        dynamicProp.stopWatch();
    }
}
