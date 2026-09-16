package com.uniportal.dao;

import com.uniportal.model.Subject;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String query = "SELECT s.*, c.course_name, f.full_name as faculty_name FROM subjects s " +
                       "LEFT JOIN courses c ON s.course_id = c.id " +
                       "LEFT JOIN faculty f ON s.faculty_id = f.faculty_id " +
                       "ORDER BY s.subject_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Subject s = new Subject();
                s.setId(rs.getInt("id"));
                s.setSubjectCode(rs.getString("subject_code"));
                s.setSubjectName(rs.getString("subject_name"));
                s.setCourseId(rs.getInt("course_id"));
                s.setSemester(rs.getInt("semester"));
                s.setCredits(rs.getInt("credits"));
                s.setFacultyId(rs.getString("faculty_id"));
                s.setCourseName(rs.getString("course_name"));
                s.setFacultyName(rs.getString("faculty_name"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSubject(Subject s) {
        String query = "INSERT INTO subjects (subject_code, subject_name, course_id, semester, credits, faculty_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, s.getSubjectCode());
            stmt.setString(2, s.getSubjectName());
            stmt.setInt(3, s.getCourseId());
            stmt.setInt(4, s.getSemester());
            stmt.setInt(5, s.getCredits());
            stmt.setString(6, s.getFacultyId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubject(Subject s) {
        String query = "UPDATE subjects SET subject_code=?, subject_name=?, course_id=?, semester=?, credits=?, faculty_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, s.getSubjectCode());
            stmt.setString(2, s.getSubjectName());
            stmt.setInt(3, s.getCourseId());
            stmt.setInt(4, s.getSemester());
            stmt.setInt(5, s.getCredits());
            stmt.setString(6, s.getFacultyId());
            stmt.setInt(7, s.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubject(int id) {
        String query = "DELETE FROM subjects WHERE id=?";
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
