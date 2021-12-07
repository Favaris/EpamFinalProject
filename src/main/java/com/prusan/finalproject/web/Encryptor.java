package com.prusan.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Encryption class. Has methods for password encodings. NOT used.
 */
public abstract class Encryptor {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final String DEFAULT_ENCODING = "SHA-256";
    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance(DEFAULT_ENCODING);
        } catch (NoSuchAlgorithmException e) {
            log.error("unable to retrieve a MessageDigest for encode method {}", DEFAULT_ENCODING, e);
        }
    }

    private Encryptor() {}

    public static String encodePassword(String password) {
        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest();
        log.debug("got an array of digested bytes");
        return toHexString(bytes);
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        String result = sb.toString();
        log.debug("successfully returned a hashed a password");
        return result;
    }
}
