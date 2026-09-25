package com.uniportal.util;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.EventService;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;

public class PopulateCalendar {
    public static void main(String[] args) {
        EventService service = new EventService();
        try {
            // Delete existing records
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM academic_calendar");
            }
            
            long now = System.currentTimeMillis();
            
            // Dept 1
            CalendarEvent e1 = new CalendarEvent();
            e1.setTitle("Semester Duration");
            e1.setDescription("Fall Semester classes.");
            e1.setEventDate(new Date(now + 86400000L * 7)); // 7 days from now
            e1.setEventType("OTHER");
            e1.setDeptId(1);
            e1.setStatus("SCHEDULED");
            service.addEvent(e1);
            
            CalendarEvent e2 = new CalendarEvent();
            e2.setTitle("Unit Test Duration");
            e2.setDescription("First internal assessment unit tests.");
            e2.setEventDate(new Date(now + 86400000L * 14)); // 14 days from now
            e2.setEventType("THEORY_EXAM");
            e2.setDeptId(1);
            e2.setStatus("SCHEDULED");
            service.addEvent(e2);
            
            CalendarEvent e3 = new CalendarEvent();
            e3.setTitle("Practical Exam Duration");
            e3.setDescription("End semester practical exams.");
            e3.setEventDate(new Date(now + 86400000L * 30)); 
            e3.setEventType("PRACTICAL_EXAM");
            e3.setDeptId(1);
            e3.setStatus("SCHEDULED");
            service.addEvent(e3);

            CalendarEvent e4 = new CalendarEvent();
            e4.setTitle("Final Exam Duration");
            e4.setDescription("End semester final theory exams.");
            e4.setEventDate(new Date(now + 86400000L * 45)); 
            e4.setEventType("THEORY_EXAM");
            e4.setDeptId(1);
            e4.setStatus("SCHEDULED");
            service.addEvent(e4);

            // Dept 2
            CalendarEvent e5 = new CalendarEvent();
            e5.setTitle("Semester Duration");
            e5.setDescription("Fall Semester classes.");
            e5.setEventDate(new Date(now + 86400000L * 7)); // 7 days from now
            e5.setEventType("OTHER");
            e5.setDeptId(2);
            e5.setStatus("SCHEDULED");
            service.addEvent(e5);
            
            CalendarEvent e6 = new CalendarEvent();
            e6.setTitle("Unit Test Duration");
            e6.setDescription("First internal assessment unit tests.");
            e6.setEventDate(new Date(now + 86400000L * 14)); // 14 days from now
            e6.setEventType("THEORY_EXAM");
            e6.setDeptId(2);
            e6.setStatus("SCHEDULED");
            service.addEvent(e6);
            
            CalendarEvent e7 = new CalendarEvent();
            e7.setTitle("Practical Exam Duration");
            e7.setDescription("End semester practical exams.");
            e7.setEventDate(new Date(now + 86400000L * 30)); 
            e7.setEventType("PRACTICAL_EXAM");
            e7.setDeptId(2);
            e7.setStatus("SCHEDULED");
            service.addEvent(e7);

            CalendarEvent e8 = new CalendarEvent();
            e8.setTitle("Final Exam Duration");
            e8.setDescription("End semester final theory exams.");
            e8.setEventDate(new Date(now + 86400000L * 45)); 
            e8.setEventType("THEORY_EXAM");
            e8.setDeptId(2);
            e8.setStatus("SCHEDULED");
            service.addEvent(e8);


            System.out.println("Sample academic calendar generated for departments.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
