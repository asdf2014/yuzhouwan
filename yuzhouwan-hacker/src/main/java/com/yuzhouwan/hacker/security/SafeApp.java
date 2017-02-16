package com.yuzhouwan.hacker.security;

import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.lang.reflect.Method;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: SafeApp
 *
 * @author Benedict Jin
 * @since 2016/8/20
 */
public class SafeApp {

    private static final Logger _log = LoggerFactory.getLogger(SafeApp.class);

    private static final String keyFilename = "F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-hacker/src/main/resources/security/key.data";
    private static final String appName = "com.yuzhouwan.hacker.security.UnsafeApp";

    /**
     * @param args F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-hacker/src/main/resources/security/key.data F:/如何成为 Java 高手/笔记/Soft Engineering/Git/[code]/yuzhouwan/yuzhouwan-hacker/target/classes/com/yuzhouwan/hacker/security/SafeApp arg0 arg1 arg2
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {

        String realArgs[] = new String[args.length - 2];
        System.arraycopy(args, 2, realArgs, 0, args.length - 2);

        _log.error("[SecurityClassLoader: reading key]");

        byte rawKey[] = FileUtils.readFile(keyFilename);
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SecurityClassLoader.ALGORITHM);
        SecretKey key = keyFactory.generateSecret(dks);

        SecurityClassLoader dr = new SecurityClassLoader(key);

        _log.error("[SecurityClassLoader: loading " + appName + "]");
        Class clazz = dr.loadClass(appName);

        Class mainArgs[] = {(new String[1]).getClass()};
        Method main = clazz.getMethod("main", mainArgs);

        Object argsArray[] = {realArgs};
        _log.error("[SecurityClassLoader: running " + appName + ".main()]");

        main.invoke(null, argsArray);
    }
}
