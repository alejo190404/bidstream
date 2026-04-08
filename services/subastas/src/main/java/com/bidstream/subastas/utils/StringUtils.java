package com.bidstream.subastas.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class StringUtils {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        return RANDOM.ints(length, 0, CHAR_POOL.length())
                .mapToObj(CHAR_POOL::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
