package net.qoopo.framework.random;

import java.security.SecureRandom;

public class RandomStringsGenerator {

    /**
     * Genera una cadena de aleatorios
     * 
     * @param length
     * @return
     */
    public static String generateAlphabetic(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return generate(length, leftLimit, rightLimit);
    }

    public static String generateAlphanumeric(int length) {
        int leftLimit = 48; // letter '0'
        int rightLimit = 122; // letter 'z'
        return generate(length, leftLimit, rightLimit);
    }

    public static String generateNumeric(int length) {
        int leftLimit = 48; // letter '0'
        int rightLimit = 58; // letter '9'
        return generate(length, leftLimit, rightLimit);
    }

    private static String generate(int length, int leftLimit, int rightLimit) {
        SecureRandom random = new SecureRandom();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
