package com.uniportal.service;

import com.uniportal.dao.EventDAO;
import com.uniportal.model.Event;
import java.util.List;

public class EventService {
    private EventDAO dao;
    
    public EventService() {
        this.dao = new EventDAO();
    }
    
    public List<Event> getUpcomingEvents(String studentId, int deptId) {
        return dao.getUpcomingEvents(studentId, deptId);
    }
    
    public List<Event> getAllEvents() {
        return dao.getAllEvents();
    }
    
    public void addEvent(Event e) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getEventName(), "Event Name");
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        
        if (!dao.addEvent(e)) {
            throw new RuntimeException("Failed to add event.");
        }
    }
    
    public void updateEvent(Event e) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getEventName(), "Event Name");
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        
        if (!dao.updateEvent(e)) {
            throw new RuntimeException("Failed to update event.");
        }
    }
    
    public void deleteEvent(int id) {
        if (!dao.deleteEvent(id)) {
            throw new RuntimeException("Failed to delete event.");
        }
    }
    
    public List<com.uniportal.model.EventRegistration> getRegistrationsForEvent(int eventId) {
        return dao.getRegistrationsForEvent(eventId);
    }

    public boolean registerForEvent(int eventId, String studentId) {
        return dao.registerForEvent(eventId, studentId);
    }
}
