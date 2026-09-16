package com.uniportal.service;

import com.uniportal.dao.AdminDAO;
import com.uniportal.model.Admin;

public class AdminService {
    
    private AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public Admin getAdminProfile(int userId) {
        return adminDAO.getAdminByUserId(userId);
    }

    public void updateAdmin(Admin admin) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(admin.getFullName(), "Full Name");
        if (!adminDAO.updateAdmin(admin)) {
            throw new RuntimeException("Failed to update admin profile.");
        }
    }
}
