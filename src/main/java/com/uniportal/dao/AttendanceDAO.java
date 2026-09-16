package com.uniportal.dao;

import com.uniportal.model.AttendanceRecord;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public List<AttendanceRecord> getAllRecords() {
        List<AttendanceRecord> list = new ArrayList<>();
        String query = "SELECT a.*, st.full_name as student_name, s.subject_name " +
                       "FROM attendance_records a " +
                       "JOIN students st ON a.student_id = st.student_id " +
                       "JOIN subjects s ON a.subject_id = s.id " +
                       "ORDER BY a.attendance_date DESC, st.student_id";
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
    
    public List<AttendanceRecord> getRecordsByStudent(String studentId) {
        List<AttendanceRecord> list = new ArrayList<>();
        String query = "SELECT a.*, st.full_name as student_name, s.subject_name " +
                       "FROM attendance_records a " +
                       "JOIN students st ON a.student_id = st.student_id " +
                       "JOIN subjects s ON a.subject_id = s.id " +
                       "WHERE a.student_id = ? " +
                       "ORDER BY a.attendance_date DESC";
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

    public boolean addRecord(AttendanceRecord a) {
        String query = "INSERT INTO attendance_records (student_id, subject_id, attendance_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getStudentId());
            stmt.setInt(2, a.getSubjectId());
            stmt.setDate(3, a.getAttendanceDate());
            stmt.setString(4, a.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRecord(AttendanceRecord a) {
        String query = "UPDATE attendance_records SET student_id=?, subject_id=?, attendance_date=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getStudentId());
            stmt.setInt(2, a.getSubjectId());
            stmt.setDate(3, a.getAttendanceDate());
            stmt.setString(4, a.getStatus());
            stmt.setInt(5, a.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRecord(int id) {
        String query = "DELETE FROM attendance_records WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private AttendanceRecord mapRow(ResultSet rs) throws SQLException {
        AttendanceRecord a = new AttendanceRecord();
        a.setId(rs.getInt("id"));
        a.setStudentId(rs.getString("student_id"));
        a.setSubjectId(rs.getInt("subject_id"));
        a.setAttendanceDate(rs.getDate("attendance_date"));
        a.setStatus(rs.getString("status"));
        a.setStudentName(rs.getString("student_name"));
        a.setSubjectName(rs.getString("subject_name"));
        return a;
    }
}
