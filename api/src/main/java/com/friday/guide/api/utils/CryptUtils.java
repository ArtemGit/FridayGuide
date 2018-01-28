package com.friday.guide.api.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

public class CryptUtils {

    private static Map<String, Map<Integer, Cipher>> ciphers = new HashMap<>();

    public static String encrypt(String s, byte[] key, Charset encoding) throws GeneralSecurityException, IOException {
        if (StringUtils.isBlank(s)) return s;
        Cipher cipher = getCipherEncrypt(key);
        return Base64.encodeBase64URLSafeString(cipher.doFinal(s.getBytes(encoding)));
    }

    public static String decrypt(String s, byte[] key, Charset encoding) throws GeneralSecurityException, IOException {
        if (StringUtils.isBlank(s)) return s;
        Cipher cipher = getCipherDecrypt(key);
        return new String(cipher.doFinal(Base64.decodeBase64(s)), encoding);
    }

    public static byte[] encrypt(byte[] arr, byte[] key) throws GeneralSecurityException {
        Cipher cipher = getCipherEncrypt(key);
        return cipher.doFinal(arr);
    }

    public static byte[] decrypt(byte[] arr, byte[] key) throws GeneralSecurityException {
        Cipher cipher = getCipherDecrypt(key);
        return cipher.doFinal(arr);
    }

    private static Cipher getCipherEncrypt(byte[] key) throws GeneralSecurityException {
        return getCipher(Cipher.ENCRYPT_MODE, key);
    }

    private static Cipher getCipherDecrypt(byte[] key) throws GeneralSecurityException {
        return getCipher(Cipher.DECRYPT_MODE, key);
    }

    private static Cipher getCipher(int mode, byte[] key) throws GeneralSecurityException {
        String sKey = new String(key, StandardCharsets.US_ASCII);
        Map<Integer, Cipher> byMode = ciphers.get(sKey);
        if (byMode == null) {
            byMode = new HashMap<>();
            ciphers.put(sKey, byMode);
        }
        Cipher cipher = byMode.get(mode);
        if (cipher == null) {
            if (key.length == 16 || key.length == 24 || key.length == 32) {
                SecretKeySpec keySpecDecrypt = new SecretKeySpec(key, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(mode, keySpecDecrypt);
                byMode.put(mode, cipher);
            } else {
                throw new InvalidKeyException("invalid key length " + key.length);
            }
        }
        return cipher;
    }
}
