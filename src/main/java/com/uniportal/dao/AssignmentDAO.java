package com.uniportal.dao;

import com.uniportal.model.Assignment;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    public List<Assignment> getAllAssignments() {
        List<Assignment> list = new ArrayList<>();
        String query = "SELECT a.*, s.subject_name FROM assignments a " +
                       "JOIN subjects s ON a.subject_id = s.id " +
                       "ORDER BY a.deadline DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Assignment> getAssignmentsForStudent(int courseId, int semester) {
        List<Assignment> list = new ArrayList<>();
        String query = "SELECT a.*, s.subject_name FROM assignments a " +
                       "JOIN subjects s ON a.subject_id = s.id " +
                       "WHERE s.course_id = ? AND s.semester = ? " +
                       "ORDER BY a.deadline DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            stmt.setInt(2, semester);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addAssignment(Assignment a) {
        String query = "INSERT INTO assignments (title, subject_id, description, deadline, attachment_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getTitle());
            stmt.setInt(2, a.getSubjectId());
            stmt.setString(3, a.getDescription());
            stmt.setTimestamp(4, a.getDeadline());
            stmt.setString(5, a.getAttachmentPath());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAssignment(Assignment a) {
        String query = "UPDATE assignments SET title=?, subject_id=?, description=?, deadline=?, attachment_path=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getTitle());
            stmt.setInt(2, a.getSubjectId());
            stmt.setString(3, a.getDescription());
            stmt.setTimestamp(4, a.getDeadline());
            stmt.setString(5, a.getAttachmentPath());
            stmt.setInt(6, a.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAssignment(int id) {
        String query = "DELETE FROM assignments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Assignment mapRow(ResultSet rs) throws SQLException {
        Assignment a = new Assignment();
        a.setId(rs.getInt("id"));
        a.setTitle(rs.getString("title"));
        a.setSubjectId(rs.getInt("subject_id"));
        a.setDescription(rs.getString("description"));
        a.setDeadline(rs.getTimestamp("deadline"));
        a.setAttachmentPath(rs.getString("attachment_path"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        a.setSubjectName(rs.getString("subject_name"));
        return a;
    }
}
