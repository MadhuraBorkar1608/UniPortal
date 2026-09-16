package com.uniportal.dao;

import com.uniportal.model.AssignmentSubmission;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentSubmissionDAO {

    public List<AssignmentSubmission> getSubmissionsForAssignment(int assignmentId) {
        List<AssignmentSubmission> list = new ArrayList<>();
        String query = "SELECT asb.*, st.full_name as student_name, a.title as assignment_title " +
                       "FROM assignment_submissions asb " +
                       "JOIN students st ON asb.student_id = st.student_id " +
                       "JOIN assignments a ON asb.assignment_id = a.id " +
                       "WHERE asb.assignment_id = ? " +
                       "ORDER BY asb.submission_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assignmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<AssignmentSubmission> getSubmissionsByStudent(String studentId) {
        List<AssignmentSubmission> list = new ArrayList<>();
        String query = "SELECT asb.*, st.full_name as student_name, a.title as assignment_title " +
                       "FROM assignment_submissions asb " +
                       "JOIN students st ON asb.student_id = st.student_id " +
                       "JOIN assignments a ON asb.assignment_id = a.id " +
                       "WHERE asb.student_id = ? " +
                       "ORDER BY asb.submission_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSubmission(AssignmentSubmission sub) {
        String query = "INSERT INTO assignment_submissions (assignment_id, student_id, submission_path, submission_time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sub.getAssignmentId());
            stmt.setString(2, sub.getStudentId());
            stmt.setString(3, sub.getSubmissionPath());
            stmt.setTimestamp(4, sub.getSubmissionTime());
            stmt.setString(5, sub.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubmissionGrade(int submissionId, String status, String grade, String feedback) {
        String query = "UPDATE assignment_submissions SET status=?, grade=?, feedback=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, grade);
            stmt.setString(3, feedback);
            stmt.setInt(4, submissionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private AssignmentSubmission mapRow(ResultSet rs) throws SQLException {
        AssignmentSubmission sub = new AssignmentSubmission();
        sub.setId(rs.getInt("id"));
        sub.setAssignmentId(rs.getInt("assignment_id"));
        sub.setStudentId(rs.getString("student_id"));
        sub.setSubmissionPath(rs.getString("submission_path"));
        sub.setSubmissionTime(rs.getTimestamp("submission_time"));
        sub.setStatus(rs.getString("status"));
        sub.setGrade(rs.getString("grade"));
        sub.setFeedback(rs.getString("feedback"));
        sub.setStudentName(rs.getString("student_name"));
        sub.setAssignmentTitle(rs.getString("assignment_title"));
        return sub;
    }
}
