package com.nikhil.project.utility;

import java.util.UUID;

public class ShortcodeGenerator {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateUniqueShortcode() {
        long lsb = UUID.randomUUID().getLeastSignificantBits();
        long absLsb = Math.abs(lsb);

        StringBuilder sb = new StringBuilder();
        while (absLsb > 0) {
            sb.append(BASE62_CHARS.charAt((int) (absLsb % 62)));
            absLsb /= 62;
        }

        while (sb.length() < 8) {
            sb.append('0');
        }

        return sb.reverse().toString();
    }
}
