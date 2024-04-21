package com.blueviolet.backend.common.util;

import java.util.Random;

public class RandomCodeUtil {

    public static String createRandomAlphabetsByLength(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return getRandomCodes(length, chars);
    }

    public static String createRandomCodesByLength(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return getRandomCodes(length, chars);
    }

    private static String getRandomCodes(int length, String chars) {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
