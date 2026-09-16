package com.uniportal.dao;

import com.uniportal.model.StudyMaterial;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyMaterialDAO {

    public List<StudyMaterial> getMaterialsByCourseAndSemester(int courseId, int semester) {
        List<StudyMaterial> list = new ArrayList<>();
        String query = "SELECT m.*, s.subject_name " +
                       "FROM study_material m " +
                       "JOIN subjects s ON m.subject_id = s.id " +
                       "WHERE s.course_id = ? AND s.semester = ? " +
                       "ORDER BY m.upload_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, courseId);
            stmt.setInt(2, semester);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                StudyMaterial m = new StudyMaterial();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setSubjectId(rs.getInt("subject_id"));
                m.setDescription(rs.getString("description"));
                m.setFileName(rs.getString("file_name"));
                m.setFilePath(rs.getString("file_path"));
                m.setUploadDate(rs.getTimestamp("upload_date"));
                m.setSubjectName(rs.getString("subject_name"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<StudyMaterial> getAllStudyMaterials() {
        List<StudyMaterial> list = new ArrayList<>();
        String query = "SELECT m.*, s.subject_name " +
                       "FROM study_material m " +
                       "JOIN subjects s ON m.subject_id = s.id " +
                       "ORDER BY m.upload_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                StudyMaterial m = new StudyMaterial();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setSubjectId(rs.getInt("subject_id"));
                m.setDescription(rs.getString("description"));
                m.setFileName(rs.getString("file_name"));
                m.setFilePath(rs.getString("file_path"));
                m.setUploadDate(rs.getTimestamp("upload_date"));
                m.setSubjectName(rs.getString("subject_name"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addStudyMaterial(StudyMaterial m) {
        String query = "INSERT INTO study_material (title, subject_id, description, file_name, file_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, m.getTitle());
            stmt.setInt(2, m.getSubjectId());
            stmt.setString(3, m.getDescription());
            stmt.setString(4, m.getFileName());
            stmt.setString(5, m.getFilePath());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudyMaterial(StudyMaterial m) {
        String query = "UPDATE study_material SET title=?, subject_id=?, description=?, file_name=?, file_path=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, m.getTitle());
            stmt.setInt(2, m.getSubjectId());
            stmt.setString(3, m.getDescription());
            stmt.setString(4, m.getFileName());
            stmt.setString(5, m.getFilePath());
            stmt.setInt(6, m.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudyMaterial(int id) {
        String query = "DELETE FROM study_material WHERE id=?";
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
