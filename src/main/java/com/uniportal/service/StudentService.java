package com.uniportal.service;

import com.uniportal.dao.StudentDAO;
import com.uniportal.model.Student;

public class StudentService {
    
    private StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public Student getStudentProfile(int userId) {
        return studentDAO.getStudentByUserId(userId);
    }

    public java.util.List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public void addStudent(Student s, String initialPassword) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(s.getStudentId(), "Student ID");
        com.uniportal.util.ValidationUtil.requireNonEmpty(s.getFullName(), "Full Name");
        com.uniportal.util.ValidationUtil.requireValidEmail(s.getEmail());
        com.uniportal.util.ValidationUtil.requireNonEmpty(initialPassword, "Initial Password");
        
        com.uniportal.dao.UserDAO userDAO = new com.uniportal.dao.UserDAO();
        String hash = com.uniportal.util.PasswordUtil.hashPassword(initialPassword);
        int userId = userDAO.addUser(s.getStudentId(), hash, "STUDENT"); // username is student_id by default
        
        if (userId > 0) {
            s.setUserId(userId);
            if (!studentDAO.addStudent(s)) {
                userDAO.deleteUser(userId); // rollback
                throw new RuntimeException("Failed to add student profile.");
            }
        } else {
            throw new RuntimeException("Failed to create user account. Username might already exist.");
        }
    }

    public void updateStudent(Student s) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(s.getFullName(), "Full Name");
        com.uniportal.util.ValidationUtil.requireValidEmail(s.getEmail());
        
        if (!studentDAO.updateStudent(s)) {
            throw new RuntimeException("Failed to update student.");
        }
    }

    public void deleteStudent(Student s) {
        com.uniportal.dao.UserDAO userDAO = new com.uniportal.dao.UserDAO();
        if (!userDAO.deleteUser(s.getUserId())) {
            throw new RuntimeException("Failed to delete student user account.");
        }
    }
}
