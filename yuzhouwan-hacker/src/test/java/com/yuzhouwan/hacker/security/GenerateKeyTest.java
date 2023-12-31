package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.dir.DirUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: GenerateKey Tester
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class GenerateKeyTest {

    @Ignore
    @Test
    public void generate() throws Exception {
        final String keyFile = DirUtils.RESOURCES_PATH.concat("security/key.data");
        GenerateKey.generate(keyFile);
        new File(keyFile).deleteOnExit();
    }
}
