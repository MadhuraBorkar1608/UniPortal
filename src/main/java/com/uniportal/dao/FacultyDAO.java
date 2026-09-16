package com.uniportal.dao;

import com.uniportal.model.Faculty;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {

    public List<Faculty> getAllFaculty() {
        List<Faculty> list = new ArrayList<>();
        String query = "SELECT f.*, d.dept_name FROM faculty f LEFT JOIN departments d ON f.dept_id = d.id ORDER BY f.full_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Faculty f = new Faculty();
                f.setFacultyId(rs.getString("faculty_id"));
                f.setFullName(rs.getString("full_name"));
                f.setEmail(rs.getString("email"));
                f.setPhone(rs.getString("phone"));
                f.setDeptId(rs.getInt("dept_id"));
                f.setDesignation(rs.getString("designation"));
                f.setDeptName(rs.getString("dept_name"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addFaculty(Faculty f) {
        String query = "INSERT INTO faculty (faculty_id, full_name, email, phone, dept_id, designation) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, f.getFacultyId());
            stmt.setString(2, f.getFullName());
            stmt.setString(3, f.getEmail());
            stmt.setString(4, f.getPhone());
            stmt.setInt(5, f.getDeptId());
            stmt.setString(6, f.getDesignation());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFaculty(Faculty f) {
        String query = "UPDATE faculty SET full_name=?, email=?, phone=?, dept_id=?, designation=? WHERE faculty_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, f.getFullName());
            stmt.setString(2, f.getEmail());
            stmt.setString(3, f.getPhone());
            stmt.setInt(4, f.getDeptId());
            stmt.setString(5, f.getDesignation());
            stmt.setString(6, f.getFacultyId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFaculty(String id) {
        String query = "DELETE FROM faculty WHERE faculty_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
