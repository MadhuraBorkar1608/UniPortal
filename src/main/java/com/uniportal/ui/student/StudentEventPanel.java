package com.uniportal.ui.student;

import com.uniportal.model.Event;
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
    private List<Event> eventList;

    public StudentEventPanel() {
        eventService = new EventService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Events & Activities");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Event Name", "Date", "Time", "Venue", "Dept", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        JButton registerBtn = new JButton("Register for Event");
        registerBtn.addActionListener(e -> registerEvent());
        bottomPanel.add(registerBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                eventList = eventService.getUpcomingEvents(s.getStudentId(), s.getDeptId());
                for (Event e : eventList) {
                    tableModel.addRow(new Object[]{
                        e.getEventName(), 
                        e.getEventDate().toString(), 
                        e.getEventTime().toString(),
                        e.getVenue(), 
                        e.getDeptName() != null ? e.getDeptName() : "All",
                        e.isRegistered() ? "Registered" : "Not Registered"
                    });
                }
            }
        }
    }
    
    private void registerEvent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Event e = eventList.get(row);
            if (e.isRegistered()) {
                UIUtils.showError(this, "You are already registered for this event.");
                return;
            }
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            boolean success = eventService.registerForEvent(e.getId(), s.getStudentId());
            if (success) {
                UIUtils.showSuccess(this, "Successfully registered for " + e.getEventName());
                loadData();
            } else {
                UIUtils.showError(this, "Failed to register. It may be full or deadline passed.");
            }
        } else {
            UIUtils.showError(this, "Please select an event to register.");
        }
    }
}
