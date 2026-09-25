package com.uniportal.ui.admin.notices;

import com.uniportal.model.Notice;
import com.uniportal.service.NoticeService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoticeManagementPanel extends JPanel {

    private NoticeService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public NoticeManagementPanel() {
        service = new NoticeService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel with Add Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Notice");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Title", "Category", "Department", "Publish Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Edit/Delete/Publish
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");
        StyledButton publishBtn = new StyledButton("Publish / Unpublish");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Notice n = getNoticeFromRow(row);
                showFormDialog(n);
            } else {
                UIUtils.showError(this, "Please select a notice to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this notice?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteNotice(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a notice to delete.");
            }
        });

        publishBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Notice n = getNoticeFromRow(row);
                String newStatus = "PUBLISHED".equals(n.getStatus()) ? "DRAFT" : "PUBLISHED";
                n.setStatus(newStatus);
                try {
                    service.updateNotice(n);
                    UIUtils.showSuccess(this, "Status updated to " + newStatus);
                    loadData();
                } catch (Exception ex) {
                    UIUtils.showError(this, ex.getMessage());
                }
            } else {
                UIUtils.showError(this, "Please select a notice to change status.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(publishBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    Notice n = getNoticeFromRow(row);
                    if (n != null) {
                        JOptionPane.showMessageDialog(NoticeManagementPanel.this,
                            n.getDescription(),
                            n.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Notice> list = service.getAllNotices();
        for (Notice n : list) {
            String deptName = n.getDeptName() == null ? "All Departments" : n.getDeptName();
            tableModel.addRow(new Object[]{n.getId(), n.getTitle(), n.getCategory(), deptName, n.getPublishDate(), n.getStatus()});
        }
    }

    private Notice getNoticeFromRow(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        // Better to fetch from DB or find in list to get all details like description
        List<Notice> list = service.getAllNotices();
        for(Notice n : list) {
            if(n.getId() == id) return n;
        }
        return null; // Should not happen
    }

    private void showFormDialog(Notice n) {
        NoticeFormDialog dialog = new NoticeFormDialog(SwingUtilities.getWindowAncestor(this), n, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
