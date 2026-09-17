package com.uniportal.ui.admin.events;

import com.uniportal.model.CalendarEvent;
import com.uniportal.service.EventService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class EventFormDialog extends JDialog {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> typeCombo;
    private JTextField durationField;
    private JTextField subjectIdField;
    private JTextField deptIdField;
    private JComboBox<String> statusCombo;
    
    private boolean saved = false;
    private CalendarEvent event;
    private EventService service;

    public EventFormDialog(Window owner, CalendarEvent event, EventService service) {
        super(owner, event == null ? "Add Academic Event" : "Edit Academic Event", ModalityType.APPLICATION_MODAL);
        this.event = event;
        this.service = service;
        
        setSize(500, 600);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        titleField = addField(panel, gbc, row++, "Title:");
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Description:"), gbc);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);
        row++;
        
        dateField = addField(panel, gbc, row++, "Date (YYYY-MM-DD):");
        timeField = addField(panel, gbc, row++, "Time (HH:MM):");
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Event Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"THEORY_EXAM", "PRACTICAL_EXAM", "UNIT_TEST", "HOLIDAY", "OTHER"});
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);
        row++;
        
        durationField = addField(panel, gbc, row++, "Duration (mins):");
        subjectIdField = addField(panel, gbc, row++, "Subject ID:");
        deptIdField = addField(panel, gbc, row++, "Dept ID (Optional):");
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"SCHEDULED", "COMPLETED", "CANCELLED"});
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        row++;
        
        if (event != null) {
            titleField.setText(event.getTitle());
            descriptionArea.setText(event.getDescription());
            if (event.getEventDate() != null) dateField.setText(event.getEventDate().toString());
            if (event.getEventTime() != null) timeField.setText(event.getEventTime().toString().substring(0, 5));
            typeCombo.setSelectedItem(event.getEventType());
            durationField.setText(String.valueOf(event.getDurationMinutes()));
            subjectIdField.setText(event.getSubjectId() > 0 ? String.valueOf(event.getSubjectId()) : "");
            deptIdField.setText(event.getDeptId() > 0 ? String.valueOf(event.getDeptId()) : "");
            statusCombo.setSelectedItem(event.getStatus());
        }
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);
        
        add(panel);
    }
    
    private JTextField addField(JPanel panel, GridBagConstraints gbc, int row, String label) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        JTextField field = new JTextField(20);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return field;
    }
    
    private void save() {
        try {
            if (event == null) event = new CalendarEvent();
            event.setTitle(titleField.getText());
            event.setDescription(descriptionArea.getText());
            event.setEventDate(Date.valueOf(dateField.getText()));
            event.setEventTime(Time.valueOf(timeField.getText() + ":00"));
            event.setEventType(typeCombo.getSelectedItem().toString());
            
            try {
                event.setDurationMinutes(Integer.parseInt(durationField.getText()));
            } catch (Exception e) {}
            
            try {
                event.setSubjectId(Integer.parseInt(subjectIdField.getText()));
            } catch (Exception e) {}
            
            try {
                event.setDeptId(Integer.parseInt(deptIdField.getText()));
            } catch (Exception e) {}
            
            event.setStatus(statusCombo.getSelectedItem().toString());
            
            if (event.getId() == 0) {
                service.addEvent(event);
            } else {
                service.updateEvent(event);
            }
            saved = true;
            dispose();
        } catch (Exception e) {
            UIUtils.showError(this, e.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
