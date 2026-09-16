package com.uniportal.ui.admin.faculty;

import com.uniportal.model.Faculty;
import com.uniportal.service.FacultyService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacultyManagementPanel extends JPanel {

    private FacultyService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public FacultyManagementPanel() {
        service = new FacultyService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Faculty");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Faculty ID", "Name", "Email", "Phone", "Department", "Designation", "DeptID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        // Hide DeptID column
        table.removeColumn(table.getColumnModel().getColumn(6));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Faculty f = getFacultyFromRow(row);
                showFormDialog(f);
            } else {
                UIUtils.showError(this, "Please select a faculty to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this faculty?")) {
                    String id = (String) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteFaculty(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a faculty to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Faculty> list = service.getAllFaculty();
        for (Faculty f : list) {
            tableModel.addRow(new Object[]{
                f.getFacultyId(), f.getFullName(), f.getEmail(), f.getPhone(),
                f.getDeptName(), f.getDesignation(), f.getDeptId()
            });
        }
    }

    private Faculty getFacultyFromRow(int row) {
        Faculty f = new Faculty();
        f.setFacultyId((String) tableModel.getValueAt(row, 0));
        f.setFullName((String) tableModel.getValueAt(row, 1));
        f.setEmail((String) tableModel.getValueAt(row, 2));
        f.setPhone((String) tableModel.getValueAt(row, 3));
        f.setDeptName((String) tableModel.getValueAt(row, 4));
        f.setDesignation((String) tableModel.getValueAt(row, 5));
        f.setDeptId((int) tableModel.getValueAt(row, 6)); // hidden column
        return f;
    }

    private void showFormDialog(Faculty f) {
        FacultyFormDialog dialog = new FacultyFormDialog(SwingUtilities.getWindowAncestor(this), f, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
