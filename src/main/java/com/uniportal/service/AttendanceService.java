package com.uniportal.service;

import com.uniportal.dao.AttendanceDAO;
import com.uniportal.model.AttendanceRecord;
import java.util.List;

public class AttendanceService {

    private AttendanceDAO dao;

    public AttendanceService() {
        this.dao = new AttendanceDAO();
    }

    public List<AttendanceRecord> getAllRecords() {
        return dao.getAllRecords();
    }
    
    public List<AttendanceRecord> getRecordsByStudent(String studentId) {
        return dao.getRecordsByStudent(studentId);
    }

    public void addRecord(AttendanceRecord a) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getStudentId(), "Student ID");
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getStatus(), "Status");
        if (a.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (a.getAttendanceDate() == null) {
            throw new IllegalArgumentException("Attendance Date is required.");
        }
        if (!dao.addRecord(a)) {
            throw new RuntimeException("Failed to add attendance record. Make sure this student isn't already marked for this subject on this date.");
        }
    }

    public void updateRecord(AttendanceRecord a) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getStudentId(), "Student ID");
        com.uniportal.util.ValidationUtil.requireNonEmpty(a.getStatus(), "Status");
        if (a.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (a.getAttendanceDate() == null) {
            throw new IllegalArgumentException("Attendance Date is required.");
        }
        if (!dao.updateRecord(a)) {
            throw new RuntimeException("Failed to update attendance record. Make sure this student isn't already marked for this subject on this date.");
        }
    }

    public void deleteRecord(int id) {
        if (!dao.deleteRecord(id)) {
            throw new RuntimeException("Failed to delete attendance record.");
        }
    }
}
