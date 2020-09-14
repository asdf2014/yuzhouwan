package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: SecurityClassLoader
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
class SecurityClassLoader extends ClassLoader {

    static final String ALGORITHM = "DES";
    private static final Logger _log = LoggerFactory.getLogger(SecurityClassLoader.class);
    private static final String CLASSES_PATH = "F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/"
            + "yuzhouwan/yuzhouwan-hacker/target/classes/com/yuzhouwan/hacker/security/";

    private Cipher cipher;

    SecurityClassLoader(SecretKey key) throws GeneralSecurityException {
        _log.error("[SecurityClassLoader: creating cipher]");
        cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());
    }

    @Override
    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            Class clazz = findLoadedClass(name);
            if (clazz != null) return clazz;
            try {
                byte[] classData;
                if (name.startsWith("com.yuzhouwan.hacker.security")) {
                    String classFile = CLASSES_PATH.concat(name.substring(name.lastIndexOf(".") + 1)).concat(".class");
                    classData = FileUtils.readFile(classFile);
                } else classData = FileUtils.readFile(name);

                if (classData != null) {
                    byte[] decryptedClassData = cipher.doFinal(classData);
                    clazz = defineClass(name, decryptedClassData, 0, decryptedClassData.length);
                    _log.error("[SecurityClassLoader: decrypting class " + name + "]");
                }
            } catch (FileNotFoundException fileNotFoundException) {
                //do nothing, just for judging security class or not
            }
            if (clazz == null) clazz = findSystemClass(name);
            if (resolve && clazz != null) resolveClass(clazz);
            return clazz;
        } catch (IOException | GeneralSecurityException e) {
            // if java.lang.ClassNotFoundException:
            // Input length must be multiple of 8 when decrypting with padded cipher,
            // then mvn clean (DO NOT mvn install!!)
            // & run com.yuzhouwan.hacker.security.GenerateKeyTest.generate to generate key
            // & run com.yuzhouwan.hacker.security.EncryptClassesTest.encrypt() to encrypt unsafe class files
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
