package com.uniportal.model;

import java.sql.Timestamp;

public class AssignmentSubmission {
    private int id;
    private int assignmentId;
    private String studentId;
    private String submissionPath;
    private Timestamp submissionTime;
    private String status;
    private String grade;
    private String feedback;

    // Derived
    private String studentName;
    private String assignmentTitle;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSubmissionPath() { return submissionPath; }
    public void setSubmissionPath(String submissionPath) { this.submissionPath = submissionPath; }

    public Timestamp getSubmissionTime() { return submissionTime; }
    public void setSubmissionTime(Timestamp submissionTime) { this.submissionTime = submissionTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getAssignmentTitle() { return assignmentTitle; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
}
