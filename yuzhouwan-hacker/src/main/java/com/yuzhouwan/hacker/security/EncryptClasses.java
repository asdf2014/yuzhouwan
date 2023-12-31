package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

import static com.yuzhouwan.hacker.security.SecurityClassLoader.ALGORITHM;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: EncryptClasses
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
class EncryptClasses {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptClasses.class);

    /**
     * @param clazz key.data UnsafeApp.class UnsafeClass.class
     */
    static void encrypt(String... clazz) throws Exception {
        String keyFilename = clazz[0];
        SecureRandom sr = new SecureRandom();
        byte[] rawKey = FileUtils.readFile(keyFilename);
        assert rawKey != null;
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM); // lgtm [java/weak-cryptographic-algorithm]
        SecretKey key = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(ALGORITHM); // lgtm [java/weak-cryptographic-algorithm]
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);

        String filename;
        byte[] classData, encryptedClassData;
        for (int i = 1; i < clazz.length; ++i) {
            filename = clazz[i];

            classData = FileUtils.readFile(filename);
            assert classData != null;
            encryptedClassData = cipher.doFinal(classData);
            FileUtils.writeFile(filename, encryptedClassData);

            LOGGER.debug("Encrypted {}", filename);
        }
    }
}
