package com.uniportal.dao;

import com.uniportal.model.Student;
import com.uniportal.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    public Student getStudentByUserId(int userId) {
        String query = "SELECT s.*, d.dept_name, c.course_name FROM students s " +
                       "LEFT JOIN departments d ON s.dept_id = d.id " +
                       "LEFT JOIN courses c ON s.course_id = c.id " +
                       "WHERE s.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.util.List<Student> getAllStudents() {
        java.util.List<Student> list = new java.util.ArrayList<>();
        String query = "SELECT s.*, d.dept_name, c.course_name FROM students s " +
                       "LEFT JOIN departments d ON s.dept_id = d.id " +
                       "LEFT JOIN courses c ON s.course_id = c.id " +
                       "ORDER BY s.full_name";
        try (Connection conn = DBConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addStudent(Student s) {
        String query = "INSERT INTO students (student_id, user_id, full_name, email, phone, dept_id, course_id, current_semester, division, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, s.getStudentId());
            stmt.setInt(2, s.getUserId());
            stmt.setString(3, s.getFullName());
            stmt.setString(4, s.getEmail());
            stmt.setString(5, s.getPhone());
            stmt.setInt(6, s.getDeptId());
            stmt.setInt(7, s.getCourseId());
            stmt.setInt(8, s.getCurrentSemester());
            stmt.setString(9, s.getDivision());
            stmt.setString(10, s.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student s) {
        String query = "UPDATE students SET full_name=?, email=?, phone=?, dept_id=?, course_id=?, current_semester=?, division=?, address=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, s.getFullName());
            stmt.setString(2, s.getEmail());
            stmt.setString(3, s.getPhone());
            stmt.setInt(4, s.getDeptId());
            stmt.setInt(5, s.getCourseId());
            stmt.setInt(6, s.getCurrentSemester());
            stmt.setString(7, s.getDivision());
            stmt.setString(8, s.getAddress());
            stmt.setString(9, s.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        String query = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setUserId(rs.getInt("user_id"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setDeptId(rs.getInt("dept_id"));
        student.setCourseId(rs.getInt("course_id"));
        student.setCurrentSemester(rs.getInt("current_semester"));
        student.setDivision(rs.getString("division"));
        student.setAddress(rs.getString("address"));
        student.setDeptName(rs.getString("dept_name"));
        student.setCourseName(rs.getString("course_name"));
        return student;
    }
}
