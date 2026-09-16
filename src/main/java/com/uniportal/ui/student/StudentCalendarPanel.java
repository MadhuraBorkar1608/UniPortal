package com.uniportal.ui.student;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.CalendarService;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentCalendarPanel extends JPanel {

    private CalendarService calendarService;
    
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentCalendarPanel() {
        calendarService = new CalendarService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Academic Calendar");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Date", "Type", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<CalendarEvent> events = calendarService.getAllEvents();
        for (CalendarEvent e : events) {
            tableModel.addRow(new Object[]{
                e.getEventDate(), e.getEventType(), e.getDescription() != null ? e.getDescription() : ""
            });
        }
    }
}
