package com.uniportal.ui.admin.attendance;

import com.uniportal.model.AttendanceRecord;
import com.uniportal.service.AttendanceService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendanceManagementPanel extends JPanel {

    private AttendanceService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public AttendanceManagementPanel() {
        service = new AttendanceService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Mark Attendance");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Student ID", "Student Name", "Subject", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                AttendanceRecord a = getRecordFromRow(row);
                showFormDialog(a);
            } else {
                UIUtils.showError(this, "Please select a record to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this record?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteRecord(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a record to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<AttendanceRecord> list = service.getAllRecords();
        for (AttendanceRecord a : list) {
            tableModel.addRow(new Object[]{
                a.getId(), a.getAttendanceDate(), a.getStudentId(), 
                a.getStudentName(), a.getSubjectName(), a.getStatus()
            });
        }
    }

    private AttendanceRecord getRecordFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<AttendanceRecord> list = service.getAllRecords();
        for (AttendanceRecord a : list) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    private void showFormDialog(AttendanceRecord a) {
        AttendanceFormDialog dialog = new AttendanceFormDialog(SwingUtilities.getWindowAncestor(this), a, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
