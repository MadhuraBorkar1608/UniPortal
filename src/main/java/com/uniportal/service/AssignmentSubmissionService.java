package com.uniportal.service;

import com.uniportal.dao.AssignmentSubmissionDAO;
import com.uniportal.model.AssignmentSubmission;
import java.util.List;

public class AssignmentSubmissionService {

    private AssignmentSubmissionDAO dao;

    public AssignmentSubmissionService() {
        this.dao = new AssignmentSubmissionDAO();
    }

    public List<AssignmentSubmission> getSubmissionsForAssignment(int assignmentId) {
        return dao.getSubmissionsForAssignment(assignmentId);
    }
    
    public List<AssignmentSubmission> getSubmissionsByStudent(String studentId) {
        return dao.getSubmissionsByStudent(studentId);
    }

    public void addSubmission(AssignmentSubmission sub) {
        if (sub.getAssignmentId() <= 0) throw new IllegalArgumentException("Assignment ID is required.");
        com.uniportal.util.ValidationUtil.requireNonEmpty(sub.getStudentId(), "Student ID");
        com.uniportal.util.ValidationUtil.requireNonEmpty(sub.getSubmissionPath(), "Submission File");
        if (sub.getSubmissionTime() == null) throw new IllegalArgumentException("Submission Time is required.");
        
        if (!dao.addSubmission(sub)) {
            throw new RuntimeException("Failed to add submission. Student may have already submitted.");
        }
    }

    public void gradeSubmission(int submissionId, String status, String grade, String feedback) {
        if (submissionId <= 0) throw new IllegalArgumentException("Invalid submission ID.");
        com.uniportal.util.ValidationUtil.requireNonEmpty(status, "Status");
        
        if (!dao.updateSubmissionGrade(submissionId, status, grade, feedback)) {
            throw new RuntimeException("Failed to update grade.");
        }
    }
}
