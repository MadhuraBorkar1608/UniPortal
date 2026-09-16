package com.uniportal.ui.admin.events;

import com.uniportal.model.Event;
import com.uniportal.model.EventRegistration;
import com.uniportal.service.EventService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EventRegistrationsDialog extends JDialog {

    private Event event;
    private EventService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public EventRegistrationsDialog(Window owner, Event event) {
        super(owner, "Registrations for: " + event.getEventName(), ModalityType.APPLICATION_MODAL);
        this.event = event;
        this.service = new EventService();
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        String[] columns = {"Registration ID", "Student ID", "Student Name", "Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StyledButton closeBtn = new StyledButton("Close");
        
        closeBtn.addActionListener(e -> dispose());
        
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadData();
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<EventRegistration> list = service.getRegistrationsForEvent(event.getId());
        for (EventRegistration r : list) {
            tableModel.addRow(new Object[]{
                r.getId(), r.getStudentId(), r.getStudentName(), r.getRegistrationTime(), r.getStatus()
            });
        }
    }
}
