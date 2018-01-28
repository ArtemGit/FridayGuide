package com.friday.guide.api.data.entity.chekIn;

import org.springframework.security.crypto.keygen.BytesKeyGenerator;

import java.security.SecureRandom;

/**
 * Created by Тёма on 28.01.2018.
 */
public class SecureRandomBytesKeyGenerator  implements BytesKeyGenerator {

    private final SecureRandom random;

    private final int keyLength;

    /**
     * Creates a secure random key generator using the defaults.
     */
    public SecureRandomBytesKeyGenerator() {
        this(DEFAULT_KEY_LENGTH);
    }

    /**
     * Creates a secure random key generator with a custom key length.
     */
    public SecureRandomBytesKeyGenerator(int keyLength) {
        this.random = new SecureRandom();
        this.keyLength = keyLength;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public byte[] generateKey() {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return bytes;
    }

    private static final int DEFAULT_KEY_LENGTH = 8;

}
