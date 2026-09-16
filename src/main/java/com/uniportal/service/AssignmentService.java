package com.uniportal.service;

import com.uniportal.dao.AssignmentDAO;
import com.uniportal.model.Assignment;
import java.util.List;

public class AssignmentService {

    private AssignmentDAO dao;

    public AssignmentService() {
        this.dao = new AssignmentDAO();
    }

    public List<Assignment> getAllAssignments() {
        return dao.getAllAssignments();
    }
    
    public List<Assignment> getAssignmentsForStudent(int courseId, int semester) {
        return dao.getAssignmentsForStudent(courseId, semester);
    }

    public void addAssignment(Assignment a) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getTitle(), "Title");
        if (a.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (a.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline is required.");
        }
        if (!dao.addAssignment(a)) {
            throw new RuntimeException("Failed to add assignment.");
        }
    }

    public void updateAssignment(Assignment a) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getTitle(), "Title");
        if (a.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (a.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline is required.");
        }
        if (!dao.updateAssignment(a)) {
            throw new RuntimeException("Failed to update assignment.");
        }
    }

    public void deleteAssignment(int id) {
        if (!dao.deleteAssignment(id)) {
            throw new RuntimeException("Failed to delete assignment.");
        }
    }
}
