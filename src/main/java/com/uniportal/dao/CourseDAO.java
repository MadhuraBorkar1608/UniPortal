package com.uniportal.dao;

import com.uniportal.model.Course;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String query = "SELECT c.*, d.dept_name FROM courses c LEFT JOIN departments d ON c.dept_id = d.id ORDER BY c.course_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseName(rs.getString("course_name"));
                c.setDeptId(rs.getInt("dept_id"));
                c.setDurationYears(rs.getInt("duration_years"));
                c.setDeptName(rs.getString("dept_name"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCourse(Course c) {
        String query = "INSERT INTO courses (course_code, course_name, dept_id, duration_years) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, c.getCourseCode());
            stmt.setString(2, c.getCourseName());
            stmt.setInt(3, c.getDeptId());
            stmt.setInt(4, c.getDurationYears());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course c) {
        String query = "UPDATE courses SET course_code=?, course_name=?, dept_id=?, duration_years=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, c.getCourseCode());
            stmt.setString(2, c.getCourseName());
            stmt.setInt(3, c.getDeptId());
            stmt.setInt(4, c.getDurationYears());
            stmt.setInt(5, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        String query = "DELETE FROM courses WHERE id=?";
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
