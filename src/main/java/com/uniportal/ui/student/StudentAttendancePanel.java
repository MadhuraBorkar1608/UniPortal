package com.uniportal.ui.student;

import com.uniportal.model.AttendanceRecord;
import com.uniportal.model.Student;
import com.uniportal.service.AttendanceService;
import com.uniportal.service.StudentService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentAttendancePanel extends JPanel {

    private AttendanceService attendanceService;
    private StudentService studentService;
    
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentAttendancePanel() {
        attendanceService = new AttendanceService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Attendance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Subject", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                List<AttendanceRecord> records = attendanceService.getRecordsByStudent(s.getStudentId());
                for (AttendanceRecord r : records) {
                    tableModel.addRow(new Object[]{
                        r.getSubjectName(), r.getAttendanceDate(), r.getStatus()
                    });
                }
            }
        }
    }
}
