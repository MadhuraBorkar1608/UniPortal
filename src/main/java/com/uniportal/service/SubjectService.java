package com.uniportal.service;

import com.uniportal.dao.SubjectDAO;
import com.uniportal.model.Subject;
import com.uniportal.util.ValidationUtil;
import java.util.List;

public class SubjectService {
    
    private SubjectDAO dao;

    public SubjectService() {
        this.dao = new SubjectDAO();
    }

    public List<Subject> getAllSubjects() {
        return dao.getAllSubjects();
    }

    private void validateUniqueFacultyPerDepartment(Subject s) {
        if (s.getFacultyId() != null && !s.getFacultyId().trim().isEmpty()) {
            List<Subject> existingSubjects = dao.getAllSubjects();
            for (Subject existing : existingSubjects) {
                if (existing.getId() != s.getId() && 
                    existing.getCourseId() == s.getCourseId() && 
                    s.getFacultyId().equals(existing.getFacultyId())) {
                    throw new RuntimeException("This faculty member is already assigned to another subject in this department. Each subject must have a different faculty.");
                }
            }
        }
    }

    public void addSubject(Subject s) {
        ValidationUtil.requireNonEmpty(s.getSubjectCode(), "Subject Code");
        ValidationUtil.requireNonEmpty(s.getSubjectName(), "Subject Name");
        ValidationUtil.requirePositive(s.getCourseId(), "Course");
        ValidationUtil.requirePositive(s.getSemester(), "Semester");
        ValidationUtil.requirePositive(s.getCredits(), "Credits");
        
        validateUniqueFacultyPerDepartment(s);
        
        if (!dao.addSubject(s)) {
            throw new RuntimeException("Failed to add subject.");
        }
    }

    public void updateSubject(Subject s) {
        ValidationUtil.requireNonEmpty(s.getSubjectCode(), "Subject Code");
        ValidationUtil.requireNonEmpty(s.getSubjectName(), "Subject Name");
        ValidationUtil.requirePositive(s.getCourseId(), "Course");
        ValidationUtil.requirePositive(s.getSemester(), "Semester");
        ValidationUtil.requirePositive(s.getCredits(), "Credits");
        
        validateUniqueFacultyPerDepartment(s);
        
        if (!dao.updateSubject(s)) {
            throw new RuntimeException("Failed to update subject.");
        }
    }

    public void deleteSubject(int id) {
        if (!dao.deleteSubject(id)) {
            throw new RuntimeException("Failed to delete subject.");
        }
    }
}
