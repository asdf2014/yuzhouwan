package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.dir.DirUtils;
import org.junit.Test;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: GenerateKey Tester
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class GenerateKeyTest {

    @Test
    public void generate() throws Exception {
        GenerateKey.generate(DirUtils.RESOURCES_PATH.concat("security/key.data"));
    }
}
