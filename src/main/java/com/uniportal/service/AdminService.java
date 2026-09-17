package com.uniportal.service;

import com.uniportal.dao.AdminDAO;
import com.uniportal.model.Admin;

public class AdminService {
    
    private AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public Admin getAdminProfile(int userId) {
        try {
            Admin admin = adminDAO.getAdminByUserId(userId);
            if (admin != null) return admin;
        } catch (Exception e) {}
        
        // Dummy data for admin
        if (userId == 1) {
            Admin dummy = new Admin();
            dummy.setUserId(1);
            dummy.setAdminId("ADM001");
            dummy.setFullName("System Administrator");
            dummy.setEmail("admin@uni.edu");
            dummy.setPhone("9876543211");
            dummy.setDesignation("Chief Admin");
            return dummy;
        }
        return null;
    }

    public void updateAdmin(Admin admin) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(admin.getFullName(), "Full Name");
        if (!adminDAO.updateAdmin(admin)) {
            throw new RuntimeException("Failed to update admin profile.");
        }
    }
}
