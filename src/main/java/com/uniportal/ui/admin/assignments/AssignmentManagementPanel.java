package com.uniportal.ui.admin.assignments;

import com.uniportal.model.Assignment;
import com.uniportal.service.AssignmentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignmentManagementPanel extends JPanel {

    private AssignmentService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public AssignmentManagementPanel() {
        service = new AssignmentService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Assignment");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Subject", "Deadline", "Created At"};
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
        StyledButton viewSubmissionsBtn = new StyledButton("View Submissions");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Assignment a = getAssignmentFromRow(row);
                showFormDialog(a);
            } else {
                UIUtils.showError(this, "Please select an assignment to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this assignment?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteAssignment(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select an assignment to delete.");
            }
        });
        
        viewSubmissionsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Assignment a = getAssignmentFromRow(row);
                AssignmentSubmissionsDialog dialog = new AssignmentSubmissionsDialog(SwingUtilities.getWindowAncestor(this), a);
                dialog.setVisible(true);
            } else {
                UIUtils.showError(this, "Please select an assignment to view submissions.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(viewSubmissionsBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Assignment> list = service.getAllAssignments();
        for (Assignment a : list) {
            tableModel.addRow(new Object[]{
                a.getId(), a.getTitle(), a.getSubjectName(), a.getDeadline(), a.getCreatedAt()
            });
        }
    }

    private Assignment getAssignmentFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<Assignment> list = service.getAllAssignments();
        for (Assignment a : list) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    private void showFormDialog(Assignment a) {
        AssignmentFormDialog dialog = new AssignmentFormDialog(SwingUtilities.getWindowAncestor(this), a, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
