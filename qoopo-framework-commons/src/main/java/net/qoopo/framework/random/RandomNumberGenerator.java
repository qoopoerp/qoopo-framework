package net.qoopo.framework.random;

import java.security.SecureRandom;

public class RandomNumberGenerator {

    /**
     * Genera un número aleatorio de longitud indicada
     * @param length
     * @return
     */
    public static String generate(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digito = random.nextInt(10); // Genera un número entre 0 y 9
            sb.append(digito);
        }

        return sb.toString();
    }
}
