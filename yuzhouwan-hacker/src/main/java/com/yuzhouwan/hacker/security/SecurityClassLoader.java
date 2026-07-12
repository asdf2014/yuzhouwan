package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: SecurityClassLoader
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
class SecurityClassLoader extends ClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityClassLoader.class);

    static final String ALGORITHM = "AES";
    static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    static final int IV_LENGTH = 16;
    private static final String CLASSES_PATH = "F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/"
        + "yuzhouwan/yuzhouwan-hacker/target/classes/com/yuzhouwan/hacker/security/";

    private final SecretKey key;

    SecurityClassLoader(SecretKey key) {
        LOGGER.error("[SecurityClassLoader: holding key]");
        this.key = key;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            Class<?> clazz = findLoadedClass(name);
            if (clazz != null) return clazz;
            try {
                byte[] classData;
                if (name.startsWith("com.yuzhouwan.hacker.security")) {
                    String classFile = CLASSES_PATH.concat(name.substring(name.lastIndexOf(".") + 1)).concat(".class");
                    classData = FileUtils.readFile(classFile);
                } else classData = FileUtils.readFile(name);

                if (classData != null) {
                    // the file layout is [16-byte random IV][AES/CBC ciphertext]
                    byte[] iv = Arrays.copyOfRange(classData, 0, IV_LENGTH);
                    byte[] cipherText = Arrays.copyOfRange(classData, IV_LENGTH, classData.length);
                    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                    byte[] decryptedClassData = cipher.doFinal(cipherText);
                    clazz = defineClass(name, decryptedClassData, 0, decryptedClassData.length);
                    LOGGER.error("[SecurityClassLoader: decrypting class " + name + "]");
                }
            } catch (FileNotFoundException fileNotFoundException) {
                //do nothing, just for judging security class or not
            }
            if (clazz == null) clazz = findSystemClass(name);
            if (resolve && clazz != null) resolveClass(clazz);
            return clazz;
        } catch (IOException | GeneralSecurityException e) {
            // if java.lang.ClassNotFoundException, then mvn clean (DO NOT mvn install!!)
            // & run com.yuzhouwan.hacker.security.GenerateKeyTest.generate to generate key
            // & run com.yuzhouwan.hacker.security.EncryptClassesTest.encrypt() to encrypt unsafe class files
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
