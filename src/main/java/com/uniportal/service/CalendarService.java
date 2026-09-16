package com.uniportal.service;

import com.uniportal.dao.CalendarDAO;
import com.uniportal.model.CalendarEvent;
import java.util.List;

public class CalendarService {

    private CalendarDAO dao;

    public CalendarService() {
        this.dao = new CalendarDAO();
    }

    public List<CalendarEvent> getAllEvents() {
        return dao.getAllEvents();
    }

    public void addEvent(CalendarEvent e) {
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getEventType(), "Event Type");
        
        if (!dao.addEvent(e)) {
            throw new RuntimeException("Failed to add calendar event.");
        }
    }

    public void updateEvent(CalendarEvent e) {
        if (e.getEventDate() == null) throw new IllegalArgumentException("Event Date is required.");
        com.uniportal.util.ValidationUtil.requireNonEmpty(e.getEventType(), "Event Type");
        
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
