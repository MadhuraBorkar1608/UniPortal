package com.uniportal.util;

import com.uniportal.exception.ValidationException;

public class ValidationUtil {

    public static void requireNonEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void requireValidEmail(String email) {
        requireNonEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format.");
        }
    }

    public static void requireValidPhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("^[0-9]{10,15}$")) {
                throw new ValidationException("Invalid phone number format.");
            }
        }
    }

    public static void requirePositive(int num, String fieldName) {
        if (num <= 0) {
            throw new ValidationException(fieldName + " must be a positive number.");
        }
    }
    
    public static void requireNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new ValidationException(fieldName + " is required.");
        }
    }
}
