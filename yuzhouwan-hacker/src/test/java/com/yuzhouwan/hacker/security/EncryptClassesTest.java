package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.DirUtils;
import org.junit.Test;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: EncryptClasses Tester
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class EncryptClassesTest {

    private static final String CLASSES_PATH = DirUtils.getClassesPath().concat("/com/yuzhouwan/hacker/security/");

    @Test
    public void encrypt() throws Exception {
        EncryptClasses.encrypt(DirUtils.RESOURCES_PATH.concat("security/key.data"),
                CLASSES_PATH.concat("UnsafeApp.class"),
                CLASSES_PATH.concat("UnsafeClass.class"));
    }
}
