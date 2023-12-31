package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.lang.reflect.Method;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: SafeApp
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class SafeApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SafeApp.class);

    private static final String KEY_FILENAME = "F:/如何成为 Java 高手/笔记/Soft Engineering/Git/"
            + "[code]/yuzhouwan/yuzhouwan-hacker/src/main/resources/security/key.data";
    private static final String APP_NAME = "com.yuzhouwan.hacker.security.UnsafeApp";

    /**
     * @param args F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-hacker/
     *             src/main/resources/security/key.data F:/如何成为 Java 高手/笔记/Soft Engineering/Git/
     *             [code]/yuzhouwan/yuzhouwan-hacker/target/classes/com/yuzhouwan/hacker/security/SafeApp
     *             arg0 arg1 arg2.
     */
    public static void main(String[] args) throws Exception {

        String[] realArgs = new String[args.length - 2];
        System.arraycopy(args, 2, realArgs, 0, args.length - 2);

        LOGGER.error("[SecurityClassLoader: reading key]");

        byte[] rawKey = FileUtils.readFile(KEY_FILENAME);
        assert rawKey != null;
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SecurityClassLoader.ALGORITHM);  // lgtm [java/weak-cryptographic-algorithm]
        SecretKey key = keyFactory.generateSecret(dks);

        SecurityClassLoader dr = new SecurityClassLoader(key);

        LOGGER.error("[SecurityClassLoader: loading " + APP_NAME + "]");
        Class<?> clazz = dr.loadClass(APP_NAME);

        Class<?>[] mainArgs = {String[].class};
        Method main = clazz.getMethod("main", mainArgs);

        Object[] argsArray = {realArgs};
        LOGGER.error("[SecurityClassLoader: running " + APP_NAME + ".main()]");

        main.invoke(null, argsArray);
    }
}
