package com.uniportal.ui.admin.studymaterial;

import com.uniportal.model.StudyMaterial;
import com.uniportal.service.StudyMaterialService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudyMaterialManagementPanel extends JPanel {

    private StudyMaterialService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudyMaterialManagementPanel() {
        service = new StudyMaterialService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Study Material");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Subject", "File Name", "Upload Date"};
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
        StyledButton openBtn = new StyledButton("Open File");
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        openBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                StudyMaterial m = getMaterialFromRow(row);
                if (m != null && m.getFilePath() != null && !m.getFilePath().isEmpty()) {
                    UIUtils.openFile(this, m.getFilePath());
                } else {
                    UIUtils.showError(this, "No file is attached to this material.");
                }
            } else {
                UIUtils.showError(this, "Please select an entry first.");
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                StudyMaterial m = getMaterialFromRow(row);
                showFormDialog(m);
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
                        service.deleteStudyMaterial(id);
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

        bottomPanel.add(openBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<StudyMaterial> list = service.getAllStudyMaterials();
        for (StudyMaterial m : list) {
            tableModel.addRow(new Object[]{
                m.getId(), m.getTitle(), m.getSubjectName(), m.getFileName(), m.getUploadDate()
            });
        }
    }

    private StudyMaterial getMaterialFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<StudyMaterial> list = service.getAllStudyMaterials();
        for (StudyMaterial m : list) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    private void showFormDialog(StudyMaterial m) {
        StudyMaterialFormDialog dialog = new StudyMaterialFormDialog(SwingUtilities.getWindowAncestor(this), m, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
