package com.uniportal.service;

import com.uniportal.dao.DepartmentDAO;
import com.uniportal.model.Department;
import com.uniportal.util.ValidationUtil;
import java.util.List;

public class DepartmentService {
    
    private DepartmentDAO dao;

    public DepartmentService() {
        this.dao = new DepartmentDAO();
    }

    public List<Department> getAllDepartments() {
        return dao.getAllDepartments();
    }

    public void addDepartment(Department d) {
        ValidationUtil.requireNonEmpty(d.getDeptCode(), "Department Code");
        ValidationUtil.requireNonEmpty(d.getDeptName(), "Department Name");
        if (!dao.addDepartment(d)) {
            throw new RuntimeException("Failed to add department.");
        }
    }

    public void updateDepartment(Department d) {
        ValidationUtil.requireNonEmpty(d.getDeptCode(), "Department Code");
        ValidationUtil.requireNonEmpty(d.getDeptName(), "Department Name");
        if (!dao.updateDepartment(d)) {
            throw new RuntimeException("Failed to update department.");
        }
    }

    public void deleteDepartment(int id) {
        if (!dao.deleteDepartment(id)) {
            throw new RuntimeException("Failed to delete department.");
        }
    }
}
