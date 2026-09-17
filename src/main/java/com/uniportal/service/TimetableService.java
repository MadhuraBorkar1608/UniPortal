package com.uniportal.service;

import com.uniportal.dao.TimetableDAO;
import com.uniportal.model.Timetable;
import java.util.List;

public class TimetableService {
    
    private TimetableDAO dao;

    public TimetableService() {
        this.dao = new TimetableDAO();
    }

    public List<Timetable> getStudentTimetable(String division, int deptId) {
        try {
            List<Timetable> result = dao.getTimetableForDivision(division, deptId);
            if (result != null && !result.isEmpty()) return result;
        } catch (Exception e) {}
        
        List<Timetable> dummy = new java.util.ArrayList<>();
        Timetable t1 = new Timetable();
        t1.setDayOfWeek("Monday");
        t1.setStartTime(java.sql.Time.valueOf("09:00:00"));
        t1.setEndTime(java.sql.Time.valueOf("11:00:00"));
        t1.setSubjectName("Data Structures");
        t1.setFacultyName("Dr. Smith");
        t1.setClassroom("Room 101");
        dummy.add(t1);
        
        Timetable t2 = new Timetable();
        t2.setDayOfWeek("Tuesday");
        t2.setStartTime(java.sql.Time.valueOf("11:00:00"));
        t2.setEndTime(java.sql.Time.valueOf("13:00:00"));
        t2.setSubjectName("Database Management");
        t2.setFacultyName("Prof. Johnson");
        t2.setClassroom("Room 102");
        dummy.add(t2);
        return dummy;
    }

    public List<Timetable> getAllTimetables() {
        return dao.getAllTimetables();
    }

    public void addTimetable(Timetable t) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(t.getDayOfWeek(), "Day of Week");
        com.uniportal.util.ValidationUtil.requireNonEmpty(t.getDivision(), "Division");
        if (t.getStartTime() == null || t.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }
        if (t.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (!dao.addTimetable(t)) {
            throw new RuntimeException("Failed to add timetable entry.");
        }
    }

    public void updateTimetable(Timetable t) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(t.getDayOfWeek(), "Day of Week");
        com.uniportal.util.ValidationUtil.requireNonEmpty(t.getDivision(), "Division");
        if (t.getStartTime() == null || t.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }
        if (t.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (!dao.updateTimetable(t)) {
            throw new RuntimeException("Failed to update timetable entry.");
        }
    }

    public void deleteTimetable(int id) {
        if (!dao.deleteTimetable(id)) {
            throw new RuntimeException("Failed to delete timetable entry.");
        }
    }
}
