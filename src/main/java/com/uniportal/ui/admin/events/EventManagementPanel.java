package com.uniportal.ui.admin.events;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.EventService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EventManagementPanel extends JPanel {

    private EventService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public EventManagementPanel() {
        service = new EventService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Academic Event");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Type", "Title", "Date", "Duration", "Subject", "Status"};
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
                CalendarEvent ev = getEventFromRow(row);
                showFormDialog(ev);
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
                e.getId(), e.getEventType(), e.getTitle(), e.getEventDate(), e.getDurationMinutes() + " mins", e.getSubjectId(), e.getStatus()
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

    private void showFormDialog(CalendarEvent ev) {
        EventFormDialog dialog = new EventFormDialog(SwingUtilities.getWindowAncestor(this), ev, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
