package com.uniportal.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }
    
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Fallback for plain text just in case DB is modified
        if (plainTextPassword.equals(hashedPassword)) {
            return true;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("BCrypt error: " + e.getMessage());
            return false;
        }
    }
}
