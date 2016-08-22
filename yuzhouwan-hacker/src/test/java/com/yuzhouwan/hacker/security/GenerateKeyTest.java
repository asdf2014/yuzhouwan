package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.DirUtils;
import org.junit.Test;

/**
 * Copyright @ 2016 yuzhouwan.com
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
