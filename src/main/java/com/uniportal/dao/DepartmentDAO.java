package com.uniportal.dao;

import com.uniportal.model.Department;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String query = "SELECT * FROM departments ORDER BY dept_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Department d = new Department();
                d.setId(rs.getInt("id"));
                d.setDeptCode(rs.getString("dept_code"));
                d.setDeptName(rs.getString("dept_name"));
                d.setDescription(rs.getString("description"));
                d.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addDepartment(Department d) {
        String query = "INSERT INTO departments (dept_code, dept_name, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, d.getDeptCode());
            stmt.setString(2, d.getDeptName());
            stmt.setString(3, d.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDepartment(Department d) {
        String query = "UPDATE departments SET dept_code=?, dept_name=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, d.getDeptCode());
            stmt.setString(2, d.getDeptName());
            stmt.setString(3, d.getDescription());
            stmt.setInt(4, d.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDepartment(int id) {
        String query = "DELETE FROM departments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
