package com.uniportal.dao;

import com.uniportal.model.Admin;
import com.uniportal.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public Admin getAdminByUserId(int userId) {
        String query = "SELECT * FROM admins WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getString("admin_id"));
                admin.setUserId(rs.getInt("user_id"));
                admin.setFullName(rs.getString("full_name"));
                admin.setEmail(rs.getString("email"));
                admin.setPhone(rs.getString("phone"));
                admin.setDesignation(rs.getString("designation"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE admins SET full_name=?, phone=?, designation=? WHERE admin_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getPhone());
            stmt.setString(3, admin.getDesignation());
            stmt.setString(4, admin.getAdminId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
