package com.uniportal.ui.admin.departments;

import com.uniportal.model.Department;
import com.uniportal.service.DepartmentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DepartmentManagementPanel extends JPanel {

    private DepartmentService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public DepartmentManagementPanel() {
        service = new DepartmentService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel removed as Add Department is no longer allowed

        // Table
        String[] columns = {"ID", "Code", "Name", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        // Remove default sorting as per spec
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Edit/Delete
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Department d = getDepartmentFromRow(row);
                showFormDialog(d);
            } else {
                UIUtils.showError(this, "Please select a department to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this department?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteDepartment(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a department to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Department> list = service.getAllDepartments();
        for (Department d : list) {
            tableModel.addRow(new Object[]{d.getId(), d.getDeptCode(), d.getDeptName(), d.getDescription()});
        }
    }

    private Department getDepartmentFromRow(int row) {
        Department d = new Department();
        d.setId((int) tableModel.getValueAt(row, 0));
        d.setDeptCode((String) tableModel.getValueAt(row, 1));
        d.setDeptName((String) tableModel.getValueAt(row, 2));
        d.setDescription((String) tableModel.getValueAt(row, 3));
        return d;
    }

    private void showFormDialog(Department d) {
        DepartmentFormDialog dialog = new DepartmentFormDialog(SwingUtilities.getWindowAncestor(this), d, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
