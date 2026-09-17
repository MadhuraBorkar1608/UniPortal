package com.uniportal.ui.student;

import com.uniportal.model.CalendarEvent;
import com.uniportal.model.Student;
import com.uniportal.service.EventService;
import com.uniportal.service.StudentService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentEventPanel extends JPanel {

    private EventService eventService;
    private StudentService studentService;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<CalendarEvent> eventList;

    public StudentEventPanel() {
        eventService = new EventService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Academic Calendar");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Type", "Event Title", "Date", "Time", "Duration", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                eventList = eventService.getUpcomingEvents(s.getStudentId(), s.getDeptId());
                for (CalendarEvent e : eventList) {
                    tableModel.addRow(new Object[]{
                        e.getEventType(),
                        e.getTitle(), 
                        e.getEventDate().toString(), 
                        e.getEventTime().toString(),
                        e.getDurationMinutes() + " mins", 
                        e.getStatus()
                    });
                }
            }
        }
    }
}
