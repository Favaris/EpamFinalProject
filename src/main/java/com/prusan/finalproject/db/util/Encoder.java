package com.prusan.finalproject.db.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Encryption class. Has methods for password encodings. NOT used.
 */
public abstract class Encoder {
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

    private Encoder() {}

    public static byte[] encodePassword(String password) {
        md.update(password.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }
}
