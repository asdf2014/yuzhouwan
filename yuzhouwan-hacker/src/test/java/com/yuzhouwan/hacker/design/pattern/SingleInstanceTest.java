package com.yuzhouwan.hacker.design.pattern;

import com.yuzhouwan.common.util.DirUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: SingleInstance Tester
 *
 * @author Benedict Jin
 * @since 2016/8/3
 */
public class SingleInstanceTest {

    private static final String SERIALIZE_STORE = DirUtils.TEST_RESOURCES_PATH.concat("serialize/serialize.out");

    @Test
    public void serialize() throws Exception {

        SingleInstance instance = SingleInstance.INSTANCE;

        try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(SERIALIZE_STORE));
             ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream(SERIALIZE_STORE))) {
            oos1.writeObject(instance);
            assertEquals(true, 3 == ((SingleInstance) ois1.readObject()).getPoint());
        }
    }
}
