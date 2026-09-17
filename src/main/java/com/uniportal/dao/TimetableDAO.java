package com.uniportal.dao;

import com.uniportal.model.Timetable;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    public List<Timetable> getTimetableForDivision(String division, int deptId) {
        List<Timetable> list = new ArrayList<>();
        String query = "SELECT t.*, s.subject_name, f.full_name as faculty_name " +
                       "FROM college_timetable t " +
                       "JOIN subjects s ON t.subject_id = s.id " +
                       "JOIN courses c ON s.course_id = c.id " +
                       "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id " +
                       "WHERE t.division = ? AND c.dept_id = ? " +
                       "ORDER BY CASE t.day_of_week WHEN 'MONDAY' THEN 1 WHEN 'TUESDAY' THEN 2 WHEN 'WEDNESDAY' THEN 3 WHEN 'THURSDAY' THEN 4 WHEN 'FRIDAY' THEN 5 WHEN 'SATURDAY' THEN 6 ELSE 7 END, t.start_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, division);
            stmt.setInt(2, deptId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Timetable t = new Timetable();
                t.setId(rs.getInt("id"));
                t.setDayOfWeek(rs.getString("day_of_week"));
                t.setStartTime(rs.getTime("start_time"));
                t.setEndTime(rs.getTime("end_time"));
                t.setSubjectId(rs.getInt("subject_id"));
                t.setFacultyId(rs.getString("faculty_id"));
                t.setClassroom(rs.getString("classroom"));
                t.setDivision(rs.getString("division"));
                t.setSubjectName(rs.getString("subject_name"));
                t.setFacultyName(rs.getString("faculty_name"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Timetable> getAllTimetables() {
        List<Timetable> list = new ArrayList<>();
        String query = "SELECT t.*, s.subject_name, f.full_name as faculty_name, d.dept_name " +
                       "FROM college_timetable t " +
                       "JOIN subjects s ON t.subject_id = s.id " +
                       "JOIN courses c ON s.course_id = c.id " +
                       "JOIN departments d ON c.dept_id = d.id " +
                       "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id " +
                       "ORDER BY d.dept_name, t.division, CASE t.day_of_week WHEN 'MONDAY' THEN 1 WHEN 'TUESDAY' THEN 2 WHEN 'WEDNESDAY' THEN 3 WHEN 'THURSDAY' THEN 4 WHEN 'FRIDAY' THEN 5 WHEN 'SATURDAY' THEN 6 ELSE 7 END, t.start_time";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Timetable t = new Timetable();
                t.setId(rs.getInt("id"));
                t.setDayOfWeek(rs.getString("day_of_week"));
                t.setStartTime(rs.getTime("start_time"));
                t.setEndTime(rs.getTime("end_time"));
                t.setSubjectId(rs.getInt("subject_id"));
                t.setFacultyId(rs.getString("faculty_id"));
                t.setClassroom(rs.getString("classroom"));
                t.setDivision(rs.getString("division"));
                t.setSubjectName(rs.getString("subject_name"));
                t.setFacultyName(rs.getString("faculty_name"));
                t.setDeptName(rs.getString("dept_name"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addTimetable(Timetable t) {
        String query = "INSERT INTO college_timetable (day_of_week, start_time, end_time, subject_id, faculty_id, classroom, division) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, t.getDayOfWeek());
            stmt.setTime(2, t.getStartTime());
            stmt.setTime(3, t.getEndTime());
            stmt.setInt(4, t.getSubjectId());
            if (t.getFacultyId() != null && !t.getFacultyId().trim().isEmpty()) {
                stmt.setString(5, t.getFacultyId());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            stmt.setString(6, t.getClassroom());
            stmt.setString(7, t.getDivision());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTimetable(Timetable t) {
        String query = "UPDATE college_timetable SET day_of_week=?, start_time=?, end_time=?, subject_id=?, faculty_id=?, classroom=?, division=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, t.getDayOfWeek());
            stmt.setTime(2, t.getStartTime());
            stmt.setTime(3, t.getEndTime());
            stmt.setInt(4, t.getSubjectId());
            if (t.getFacultyId() != null && !t.getFacultyId().trim().isEmpty()) {
                stmt.setString(5, t.getFacultyId());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            stmt.setString(6, t.getClassroom());
            stmt.setString(7, t.getDivision());
            stmt.setInt(8, t.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTimetable(int id) {
        String query = "DELETE FROM college_timetable WHERE id=?";
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
