package com.uniportal.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class CalendarEvent {
    private int id;
    private String title;
    private String description;
    private Date eventDate;
    private Time eventTime;
    private String eventType; // 'THEORY_EXAM', 'PRACTICAL_EXAM', 'UNIT_TEST', 'HOLIDAY', 'OTHER'
    private int durationMinutes;
    private int subjectId;
    private int deptId;
    private String status;
    private Timestamp createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public Time getEventTime() { return eventTime; }
    public void setEventTime(Time eventTime) { this.eventTime = eventTime; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
