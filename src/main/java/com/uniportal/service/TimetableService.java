package com.uniportal.service;

import com.uniportal.dao.TimetableDAO;
import com.uniportal.model.Timetable;
import java.util.List;

public class TimetableService {
    
    private TimetableDAO dao;

    public TimetableService() {
        this.dao = new TimetableDAO();
    }

    public List<Timetable> getStudentTimetable(String division, int courseId, int semester) {
        return dao.getTimetableForDivision(division, courseId, semester);
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
