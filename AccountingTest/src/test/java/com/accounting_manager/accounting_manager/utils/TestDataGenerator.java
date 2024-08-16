package com.accounting_manager.accounting_manager.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestDataGenerator {

    public static String generateUniqueEmail() {
        String uniqueIdentifier = generateUniqueIdentifier();
        String uniqueEmail = "test" + uniqueIdentifier + "@example.com";
        return uniqueEmail;
    }

    private static String generateUniqueIdentifier() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = now.format(formatter);
        return timestamp;
    }

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateAlphaString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
}