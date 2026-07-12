package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: GenerateKey
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
class GenerateKey {

    /**
     * @param keyFileName key.data
     */
    static void generate(String keyFileName) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(SecurityClassLoader.ALGORITHM);
        kg.init(128, new SecureRandom());
        SecretKey key = kg.generateKey();
        FileUtils.writeFile(keyFileName, key.getEncoded());
    }
}
