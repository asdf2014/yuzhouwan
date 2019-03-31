package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Md5 Utils
 *
 * @author Benedict Jin
 * @since 2017/12/7
 */
public final class Md5Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Md5Utils.class);
    private static final String MD5 = "MD5";

    private Md5Utils() {
    }

    public static String md5(String s) {
        if (s == null) {
            LOG.warn("Need not-null input in md5 method!");
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(MD5);
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes, 0, bytes.length);
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (Exception e) {
            LOG.error(String.format("Cannot process md5 method, will return the origin string: %s!", s), e);
            return s;
        }
    }
}
