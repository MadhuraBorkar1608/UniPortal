package com.uniportal.ui.admin.calendar;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.CalendarService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CalendarManagementPanel extends JPanel {

    private CalendarService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public CalendarManagementPanel() {
        service = new CalendarService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Event");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Event Type", "Description"};
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
                CalendarEvent ce = getEventFromRow(row);
                showFormDialog(ce);
            } else {
                UIUtils.showError(this, "Please select an event to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this event?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteEvent(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select an event to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<CalendarEvent> list = service.getAllEvents();
        for (CalendarEvent e : list) {
            tableModel.addRow(new Object[]{
                e.getId(), e.getEventDate(), e.getEventType(), e.getDescription()
            });
        }
    }

    private CalendarEvent getEventFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<CalendarEvent> list = service.getAllEvents();
        for (CalendarEvent e : list) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    private void showFormDialog(CalendarEvent e) {
        CalendarFormDialog dialog = new CalendarFormDialog(SwingUtilities.getWindowAncestor(this), e, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
