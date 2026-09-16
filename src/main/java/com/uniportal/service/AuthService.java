package com.uniportal.service;

import com.uniportal.dao.UserDAO;
import com.uniportal.exception.ValidationException;
import com.uniportal.model.User;
import com.uniportal.util.PasswordUtil;
import com.uniportal.util.SessionManager;
import com.uniportal.util.ValidationUtil;

public class AuthService {
    
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        try {
            // BACKDOOR for testing if the hash is completely broken:
            if ("admin".equals(username) && "password".equals(password)) {
                User dummyAdmin = new User();
                dummyAdmin.setId(1);
                dummyAdmin.setUsername("admin");
                dummyAdmin.setRole("ADMIN");
                SessionManager.login(dummyAdmin);
                return true;
            }
            if ("STU001".equals(username) && "password".equals(password)) {
                User dummyStudent = new User();
                dummyStudent.setId(2);
                dummyStudent.setUsername("STU001");
                dummyStudent.setRole("STUDENT");
                SessionManager.login(dummyStudent);
                return true;
            }

            User user = userDAO.getUserByUsername(username);
            
            if (user == null) {
                throw new ValidationException("User not found in database. Did you run the SQL script?");
            }
            
            if ("INACTIVE".equalsIgnoreCase(user.getStatus())) {
                throw new ValidationException("Account is disabled. Please contact administration.");
            }
            
            if (PasswordUtil.checkPassword(password, user.getPasswordHash())) {
                SessionManager.login(user);
                return true;
            } else {
                throw new ValidationException("Password does not match.");
            }
        } catch (ValidationException ve) {
            throw ve;
        } catch (Exception e) {
            throw new ValidationException("Database Error: " + e.getMessage());
        }
    }
    
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            throw new ValidationException("No active session.");
        }
        
        ValidationUtil.requireNonEmpty(currentPassword, "Current Password");
        ValidationUtil.requireNonEmpty(newPassword, "New Password");
        
        if (!newPassword.equals(confirmPassword)) {
            throw new ValidationException("New password and confirm password do not match.");
        }
        
        if (!PasswordUtil.checkPassword(currentPassword, user.getPasswordHash())) {
            throw new ValidationException("Incorrect current password.");
        }
        
        String newHash = PasswordUtil.hashPassword(newPassword);
        boolean success = userDAO.updatePassword(user.getId(), newHash);
        
        if (success) {
            user.setPasswordHash(newHash);
        } else {
            throw new RuntimeException("Failed to update password.");
        }
    }
}
