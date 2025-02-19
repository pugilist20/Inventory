package org.Inventory.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordGenerator {
    @Value("${spring.jwt.char-lower}")
    private String CHAR_LOWER;

    @Value("${spring.jwt.number}")
    private String NUMBER;

    @Value("${spring.jwt.other-char}")
    private String OTHER_CHAR;

    public String generateRandomPassword(int length) {
        if (length < 1) throw new IllegalArgumentException();

        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;

        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
            char rndChar = PASSWORD_ALLOW_BASE.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }
}
