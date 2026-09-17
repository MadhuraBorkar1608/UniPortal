package com.uniportal.service;

import com.uniportal.dao.EventDAO;
import com.uniportal.model.CalendarEvent;
import java.util.List;

public class EventService {
    private EventDAO dao;
    
    public EventService() {
        this.dao = new EventDAO();
    }
    
    public List<CalendarEvent> getUpcomingEvents(String studentId, int deptId) {
        return dao.getUpcomingEvents(studentId, deptId);
    }
    
    public List<CalendarEvent> getAllEvents() {
        return dao.getAllEvents();
    }
    
    public void addEvent(CalendarEvent e) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getTitle(), "Title");
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        
        if (!dao.addEvent(e)) {
            throw new RuntimeException("Failed to add calendar event.");
        }
    }
    
    public void updateEvent(CalendarEvent e) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getTitle(), "Title");
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        
        if (!dao.updateEvent(e)) {
            throw new RuntimeException("Failed to update calendar event.");
        }
    }
    
    public void deleteEvent(int id) {
        if (!dao.deleteEvent(id)) {
            throw new RuntimeException("Failed to delete calendar event.");
        }
    }
}
