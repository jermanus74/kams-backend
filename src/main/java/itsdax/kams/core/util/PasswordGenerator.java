package itsdax.kams.core.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final char[] CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public static String generateTempPassword(int length) {

        if (length < 4 || length > 6) {
            throw new IllegalArgumentException("Temporary password length must be between 4 and 6");
        }

        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }

        return password.toString();
    }

    // convenience method for your use case (6 chars like A7K4P9)
    public static String generateTempPassword() {
        return generateTempPassword(6);
    }
}