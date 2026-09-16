package com.uniportal.dao;

import com.uniportal.model.Notice;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {

    public List<Notice> getAllNotices() {
        List<Notice> list = new ArrayList<>();
        String query = "SELECT n.*, d.dept_name FROM notices n LEFT JOIN departments d ON n.dept_id = d.id ORDER BY n.publish_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToNotice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Notice> getPublishedNotices(Integer deptId) {
        List<Notice> list = new ArrayList<>();
        String query = "SELECT n.*, d.dept_name FROM notices n LEFT JOIN departments d ON n.dept_id = d.id WHERE n.status = 'PUBLISHED' AND (n.dept_id IS NULL OR n.dept_id = ?) ORDER BY n.publish_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (deptId == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, deptId);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNotice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addNotice(Notice n) {
        String query = "INSERT INTO notices (title, description, category, dept_id, publish_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, n.getTitle());
            stmt.setString(2, n.getDescription());
            stmt.setString(3, n.getCategory());
            if (n.getDeptId() != null && n.getDeptId() > 0) {
                stmt.setInt(4, n.getDeptId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setDate(5, n.getPublishDate());
            stmt.setString(6, n.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNotice(Notice n) {
        String query = "UPDATE notices SET title=?, description=?, category=?, dept_id=?, publish_date=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, n.getTitle());
            stmt.setString(2, n.getDescription());
            stmt.setString(3, n.getCategory());
            if (n.getDeptId() != null && n.getDeptId() > 0) {
                stmt.setInt(4, n.getDeptId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setDate(5, n.getPublishDate());
            stmt.setString(6, n.getStatus());
            stmt.setInt(7, n.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNotice(int id) {
        String query = "DELETE FROM notices WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Notice mapResultSetToNotice(ResultSet rs) throws SQLException {
        Notice n = new Notice();
        n.setId(rs.getInt("id"));
        n.setTitle(rs.getString("title"));
        n.setDescription(rs.getString("description"));
        n.setCategory(rs.getString("category"));
        n.setDeptId(rs.getInt("dept_id"));
        if (rs.wasNull()) {
            n.setDeptId(null);
        }
        n.setPublishDate(rs.getDate("publish_date"));
        n.setStatus(rs.getString("status"));
        n.setDeptName(rs.getString("dept_name"));
        return n;
    }
}
