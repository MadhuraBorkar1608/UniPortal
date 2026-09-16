package com.uniportal.ui.admin.timetable;

import com.uniportal.model.Timetable;
import com.uniportal.service.TimetableService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimetableManagementPanel extends JPanel {

    private TimetableService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public TimetableManagementPanel() {
        service = new TimetableService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Timetable Entry");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Division", "Day", "Time", "Subject", "Faculty", "Classroom"};
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
                Timetable t = getTimetableFromRow(row);
                showFormDialog(t);
            } else {
                UIUtils.showError(this, "Please select an entry to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this entry?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteTimetable(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select an entry to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Timetable> list = service.getAllTimetables();
        for (Timetable t : list) {
            String timeStr = t.getStartTime() + " - " + t.getEndTime();
            tableModel.addRow(new Object[]{
                t.getId(), t.getDivision(), t.getDayOfWeek(), timeStr, 
                t.getSubjectName(), t.getFacultyName(), t.getClassroom()
            });
        }
    }

    private Timetable getTimetableFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<Timetable> list = service.getAllTimetables();
        for(Timetable t : list) {
            if(t.getId() == id) return t;
        }
        return null;
    }

    private void showFormDialog(Timetable t) {
        TimetableFormDialog dialog = new TimetableFormDialog(SwingUtilities.getWindowAncestor(this), t, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
