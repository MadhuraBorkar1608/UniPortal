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

    public void addSubject(Subject s) {
        ValidationUtil.requireNonEmpty(s.getSubjectCode(), "Subject Code");
        ValidationUtil.requireNonEmpty(s.getSubjectName(), "Subject Name");
        ValidationUtil.requirePositive(s.getCourseId(), "Course");
        ValidationUtil.requirePositive(s.getSemester(), "Semester");
        ValidationUtil.requirePositive(s.getCredits(), "Credits");
        
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
