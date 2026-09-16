package com.uniportal.service;

import com.uniportal.dao.CourseDAO;
import com.uniportal.model.Course;
import com.uniportal.util.ValidationUtil;
import java.util.List;

public class CourseService {
    
    private CourseDAO dao;

    public CourseService() {
        this.dao = new CourseDAO();
    }

    public List<Course> getAllCourses() {
        return dao.getAllCourses();
    }

    public void addCourse(Course c) {
        ValidationUtil.requireNonEmpty(c.getCourseCode(), "Course Code");
        ValidationUtil.requireNonEmpty(c.getCourseName(), "Course Name");
        ValidationUtil.requirePositive(c.getDeptId(), "Department");
        ValidationUtil.requirePositive(c.getDurationYears(), "Duration");
        
        if (!dao.addCourse(c)) {
            throw new RuntimeException("Failed to add course.");
        }
    }

    public void updateCourse(Course c) {
        ValidationUtil.requireNonEmpty(c.getCourseCode(), "Course Code");
        ValidationUtil.requireNonEmpty(c.getCourseName(), "Course Name");
        ValidationUtil.requirePositive(c.getDeptId(), "Department");
        ValidationUtil.requirePositive(c.getDurationYears(), "Duration");
        
        if (!dao.updateCourse(c)) {
            throw new RuntimeException("Failed to update course.");
        }
    }

    public void deleteCourse(int id) {
        if (!dao.deleteCourse(id)) {
            throw new RuntimeException("Failed to delete course.");
        }
    }
}
