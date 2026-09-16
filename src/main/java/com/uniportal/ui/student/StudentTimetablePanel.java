package com.uniportal.ui.student;

import com.uniportal.model.Student;
import com.uniportal.model.Timetable;
import com.uniportal.service.StudentService;
import com.uniportal.service.TimetableService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentTimetablePanel extends JPanel {

    private TimetableService timetableService;
    private StudentService studentService;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentTimetablePanel() {
        timetableService = new TimetableService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Class Timetable");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Day", "Start Time", "End Time", "Subject", "Faculty", "Classroom"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false); // disable sorting to keep day order

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                List<Timetable> schedule = timetableService.getStudentTimetable(s.getDivision(), s.getCourseId(), s.getCurrentSemester());
                for (Timetable t : schedule) {
                    tableModel.addRow(new Object[]{
                        t.getDayOfWeek(), 
                        t.getStartTime().toString(), 
                        t.getEndTime().toString(),
                        t.getSubjectName(), 
                        t.getFacultyName() != null ? t.getFacultyName() : "N/A", 
                        t.getClassroom()
                    });
                }
            }
        }
    }
}
