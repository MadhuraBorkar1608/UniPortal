package com.uniportal.ui.admin.calendar;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.CalendarService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class CalendarFormDialog extends JDialog {

    private JTextField dateField;
    private JTextField typeField;
    private JTextArea descArea;
    
    private CalendarEvent event;
    private CalendarService service;
    private boolean saved = false;

    public CalendarFormDialog(Window owner, CalendarEvent event, CalendarService service) {
        super(owner, event == null ? "Add Calendar Event" : "Edit Calendar Event", ModalityType.APPLICATION_MODAL);
        this.event = event;
        this.service = service;
        
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(dateField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Event Type:"), gbc);
        typeField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(typeField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        gbc.gridx = 1; formPanel.add(new JScrollPane(descArea), gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (event != null) {
            dateField.setText(event.getEventDate().toString());
            typeField.setText(event.getEventType());
            descArea.setText(event.getDescription());
        } else {
            dateField.setText(new Date(System.currentTimeMillis()).toString());
        }
    }
    
    private void save() {
        try {
            CalendarEvent e = event == null ? new CalendarEvent() : event;
            e.setEventDate(Date.valueOf(dateField.getText().trim()));
            e.setEventType(typeField.getText().trim());
            e.setDescription(descArea.getText().trim());
            
            if (event == null) {
                service.addEvent(e);
            } else {
                service.updateEvent(e);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            UIUtils.showError(this, "Invalid date format. Use YYYY-MM-DD.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
