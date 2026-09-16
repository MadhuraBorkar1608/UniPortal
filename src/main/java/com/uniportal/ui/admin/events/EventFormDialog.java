package com.uniportal.ui.admin.events;

import com.uniportal.model.Department;
import com.uniportal.model.Event;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.EventService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class EventFormDialog extends JDialog {

    private JTextField nameField;
    private JTextArea descArea;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JTextField organizerField;
    private JComboBox<DeptItem> deptCombo;
    private JTextField capacityField;
    private JComboBox<String> statusCombo;
    private JTextField regDeadlineField;
    
    private Event event;
    private EventService service;
    private boolean saved = false;

    public EventFormDialog(Window owner, Event event, EventService service) {
        super(owner, event == null ? "Add Event" : "Edit Event", ModalityType.APPLICATION_MODAL);
        this.event = event;
        this.service = service;
        
        setSize(500, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Event Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(dateField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Time (HH:MM:SS):"), gbc);
        timeField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(timeField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Venue:"), gbc);
        venueField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(venueField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Organizer:"), gbc);
        organizerField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(organizerField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Department:"), gbc);
        deptCombo = new JComboBox<>();
        deptCombo.addItem(new DeptItem(0, "All Departments (College-Wide)"));
        loadDepts();
        gbc.gridx = 1; formPanel.add(deptCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Capacity:"), gbc);
        capacityField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(capacityField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Reg Deadline (YYYY-MM-DD HH:MM:SS):"), gbc);
        regDeadlineField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(regDeadlineField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"DRAFT", "PUBLISHED", "COMPLETED", "CANCELLED"});
        gbc.gridx = 1; formPanel.add(statusCombo, gbc);
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
            nameField.setText(event.getEventName());
            dateField.setText(event.getEventDate() != null ? event.getEventDate().toString() : "");
            timeField.setText(event.getEventTime() != null ? event.getEventTime().toString() : "");
            venueField.setText(event.getVenue());
            organizerField.setText(event.getOrganizer());
            capacityField.setText(String.valueOf(event.getCapacity()));
            regDeadlineField.setText(event.getRegistrationDeadline() != null ? event.getRegistrationDeadline().toString() : "");
            statusCombo.setSelectedItem(event.getStatus());
            descArea.setText(event.getDescription());
            
            for (int i = 0; i < deptCombo.getItemCount(); i++) {
                if (event.getDeptId() != null && deptCombo.getItemAt(i).id == event.getDeptId()) {
                    deptCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void loadDepts() {
        DepartmentService ds = new DepartmentService();
        List<Department> list = ds.getAllDepartments();
        for (Department d : list) {
            deptCombo.addItem(new DeptItem(d.getId(), d.getDeptName()));
        }
    }
    
    private void save() {
        try {
            Event e = event == null ? new Event() : event;
            e.setEventName(nameField.getText().trim());
            e.setDescription(descArea.getText().trim());
            
            if (!dateField.getText().trim().isEmpty()) {
                e.setEventDate(Date.valueOf(dateField.getText().trim()));
            }
            if (!timeField.getText().trim().isEmpty()) {
                e.setEventTime(Time.valueOf(timeField.getText().trim()));
            }
            
            e.setVenue(venueField.getText().trim());
            e.setOrganizer(organizerField.getText().trim());
            
            DeptItem selDept = (DeptItem) deptCombo.getSelectedItem();
            if (selDept != null && selDept.id > 0) {
                e.setDeptId(selDept.id);
            } else {
                e.setDeptId(null);
            }
            
            e.setCapacity(capacityField.getText().trim().isEmpty() ? 0 : Integer.parseInt(capacityField.getText().trim()));
            
            if (!regDeadlineField.getText().trim().isEmpty()) {
                e.setRegistrationDeadline(Timestamp.valueOf(regDeadlineField.getText().trim()));
            }
            
            e.setStatus((String) statusCombo.getSelectedItem());
            
            if (event == null) {
                service.addEvent(e);
            } else {
                service.updateEvent(e);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            UIUtils.showError(this, "Invalid date/time/number format.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    private static class DeptItem {
        int id;
        String name;
        DeptItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}
