package com.uniportal.model;

import java.sql.Timestamp;

public class EventRegistration {
    private int id;
    private int eventId;
    private String studentId;
    private Timestamp registrationTime;
    private String status;

    // Derived
    private String studentName;
    private String eventName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Timestamp getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(Timestamp registrationTime) { this.registrationTime = registrationTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
}
