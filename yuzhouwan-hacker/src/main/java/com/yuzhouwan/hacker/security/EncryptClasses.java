package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: EncryptClasses
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
class EncryptClasses {

    private static final Logger _log = LoggerFactory.getLogger(EncryptClasses.class);

    private static final String algorithm = SecurityClassLoader.ALGORITHM;

    /**
     * @param clazz key.data UnsafeApp.class UnsafeClass.class
     * @throws Exception
     */
    static void encrypt(String... clazz) throws Exception {
        String keyFilename = clazz[0];
        SecureRandom sr = new SecureRandom();
        byte[] rawKey = FileUtils.readFile(keyFilename);
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);

        String filename;
        byte[] classData, encryptedClassData;
        for (int i = 1; i < clazz.length; ++i) {
            filename = clazz[i];

            classData = FileUtils.readFile(filename);
            encryptedClassData = cipher.doFinal(classData);
            FileUtils.writeFile(filename, encryptedClassData);

            _log.debug("Encrypted {}", filename);
        }
    }
}
