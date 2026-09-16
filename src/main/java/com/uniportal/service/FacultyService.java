package com.uniportal.service;

import com.uniportal.dao.FacultyDAO;
import com.uniportal.model.Faculty;
import com.uniportal.util.ValidationUtil;
import java.util.List;

public class FacultyService {
    
    private FacultyDAO dao;

    public FacultyService() {
        this.dao = new FacultyDAO();
    }

    public List<Faculty> getAllFaculty() {
        return dao.getAllFaculty();
    }

    public void addFaculty(Faculty f) {
        ValidationUtil.requireNonEmpty(f.getFacultyId(), "Faculty ID");
        ValidationUtil.requireNonEmpty(f.getFullName(), "Full Name");
        ValidationUtil.requireValidEmail(f.getEmail());
        ValidationUtil.requireValidPhone(f.getPhone());
        ValidationUtil.requirePositive(f.getDeptId(), "Department");
        
        if (!dao.addFaculty(f)) {
            throw new RuntimeException("Failed to add faculty.");
        }
    }

    public void updateFaculty(Faculty f) {
        ValidationUtil.requireNonEmpty(f.getFacultyId(), "Faculty ID");
        ValidationUtil.requireNonEmpty(f.getFullName(), "Full Name");
        ValidationUtil.requireValidEmail(f.getEmail());
        ValidationUtil.requireValidPhone(f.getPhone());
        ValidationUtil.requirePositive(f.getDeptId(), "Department");
        
        if (!dao.updateFaculty(f)) {
            throw new RuntimeException("Failed to update faculty.");
        }
    }

    public void deleteFaculty(String id) {
        if (!dao.deleteFaculty(id)) {
            throw new RuntimeException("Failed to delete faculty.");
        }
    }
}
