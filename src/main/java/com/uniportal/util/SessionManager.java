package com.uniportal.util;

import com.uniportal.model.User;

public class SessionManager {
    
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }
    
    public static boolean hasRole(String role) {
        return currentUser != null && role.equalsIgnoreCase(currentUser.getRole());
    }
}
