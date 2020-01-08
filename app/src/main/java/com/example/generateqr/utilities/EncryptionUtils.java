package com.example.generateqr.utilities;

import android.util.Base64;

public class EncryptionUtils {

    static String key = "FrontRowApp";

    private static EncryptionUtils instance;

    public static EncryptionUtils getInstance() {
        if (instance == null) {
            instance = new EncryptionUtils();
        }
        return instance;
    }

    private EncryptionUtils() {
    }

    public String encrypt(int data) {
        return Base64.encodeToString(String.valueOf(data).getBytes(), Base64.NO_WRAP);
    }

    public String encrypt(String data) {
        return Base64.encodeToString(xor(data.getBytes(), key), Base64.NO_WRAP);
    }

    public String decrypt(String data) {
        try {
            return new String(xor(Base64.decode(data.getBytes(), Base64.NO_WRAP), key), "UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static byte[] xor(final byte[] input, String key) {
        final byte[] output = new byte[input.length];
        final byte[] secret = key.getBytes();
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            spos += 1;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return output;
    }
}
