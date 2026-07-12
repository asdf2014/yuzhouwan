package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static com.yuzhouwan.hacker.security.SecurityClassLoader.ALGORITHM;
import static com.yuzhouwan.hacker.security.SecurityClassLoader.IV_LENGTH;
import static com.yuzhouwan.hacker.security.SecurityClassLoader.TRANSFORMATION;

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
        SecretKey key = new SecretKeySpec(rawKey, ALGORITHM);

        String filename;
        byte[] classData;
        for (int i = 1; i < clazz.length; ++i) {
            filename = clazz[i];

            classData = FileUtils.readFile(filename);
            assert classData != null;

            // a fresh random IV per file; store it as [16-byte IV][ciphertext]
            byte[] iv = new byte[IV_LENGTH];
            sr.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] cipherText = cipher.doFinal(classData);

            byte[] encryptedClassData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedClassData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedClassData, iv.length, cipherText.length);
            FileUtils.writeFile(filename, encryptedClassData);

            LOGGER.debug("Encrypted {}", filename);
        }
    }
}
