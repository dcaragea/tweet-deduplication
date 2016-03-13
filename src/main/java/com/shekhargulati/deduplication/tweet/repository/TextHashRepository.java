package com.shekhargulati.deduplication.tweet.repository;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public interface TextHashRepository {

    static TextHashRepository getDefaultRepository() {
        return new InmemoryTextHashRepository();
    }

    default String hash(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            byte[] bytes = text.getBytes("UTF-8");
            messageDigest.update(bytes);
            byte[] digest = messageDigest.digest();
            String hexStr = "";
            for (byte d : digest) {
                hexStr += Integer.toString((d & 0xff) + 0x100, 16).substring(1);
            }
            return hexStr;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to calculate HASH for given text", e);
        }
    }

    boolean textExists(String text);

    void add(String text);

    Set<String> getHashes();
}
