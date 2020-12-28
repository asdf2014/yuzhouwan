package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Digest Utils
 *
 * @author Benedict Jin
 * @since 2017/12/7
 */
public final class DigestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestUtils.class);
    private static final String DIGEST = "SHA-256";

    private DigestUtils() {
    }

    public static String digest(String s) {
        if (s == null) {
            LOGGER.warn("Need not-null input in digest method!");
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST);
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes, 0, bytes.length);
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot process digest method, will return the origin string: %s!", s), e);
            return s;
        }
    }
}
