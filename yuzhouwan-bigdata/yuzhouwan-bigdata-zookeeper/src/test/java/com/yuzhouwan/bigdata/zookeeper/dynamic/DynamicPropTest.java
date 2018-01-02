package com.yuzhouwan.bigdata.zookeeper.dynamic;

import com.yuzhouwan.bigdata.zookeeper.prop.DynamicProp;
import com.yuzhouwan.common.dir.DirUtils;
import org.junit.Test;

import java.io.File;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Dynamic Prop Test
 *
 * @author Benedict Jin
 * @since 2016/7/14
 */
public class DynamicPropTest {

    private static final String PROP_PATH = DirUtils.RESOURCES_PATH.concat("prop/");

    @Test
    public void dynamic() throws Exception {

        DynamicProp dynamicProp = new DynamicProp(PROP_PATH);
        try {
            dynamicProp.startWatch();

            DirUtils.makeSureExist(PROP_PATH.concat("a"), false);
            DirUtils.makeSureExist(PROP_PATH.concat("b/"), false);
            DirUtils.makeSureExist(PROP_PATH.concat("c.txt"), true);

            new File(PROP_PATH.concat("a")).delete();
            new File(PROP_PATH.concat("b/")).delete();
            new File(PROP_PATH.concat("c.txt")).delete();

            new File(PROP_PATH).deleteOnExit();
        } finally {
            Thread.sleep(50);
            dynamicProp.stopWatch();
        }
    }
}
