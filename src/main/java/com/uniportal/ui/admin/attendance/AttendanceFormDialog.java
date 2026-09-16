package com.uniportal.ui.admin.attendance;

import com.uniportal.model.AttendanceRecord;
import com.uniportal.model.Subject;
import com.uniportal.service.AttendanceService;
import com.uniportal.service.SubjectService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AttendanceFormDialog extends JDialog {

    private JTextField studentIdField;
    private JComboBox<SubjectItem> subjectCombo;
    private JTextField dateField;
    private JComboBox<String> statusCombo;
    
    private AttendanceRecord record;
    private AttendanceService service;
    private boolean saved = false;

    public AttendanceFormDialog(Window owner, AttendanceRecord record, AttendanceService service) {
        super(owner, record == null ? "Mark Attendance" : "Edit Attendance", ModalityType.APPLICATION_MODAL);
        this.record = record;
        this.service = service;
        
        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Student ID:"), gbc);
        studentIdField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(studentIdField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Subject:"), gbc);
        subjectCombo = new JComboBox<>();
        loadSubjects();
        gbc.gridx = 1; formPanel.add(subjectCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        dateField.setText(new Date(System.currentTimeMillis()).toString());
        gbc.gridx = 1; formPanel.add(dateField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"PRESENT", "ABSENT"});
        gbc.gridx = 1; formPanel.add(statusCombo, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (record != null) {
            studentIdField.setText(record.getStudentId());
            dateField.setText(record.getAttendanceDate().toString());
            statusCombo.setSelectedItem(record.getStatus());
            
            for (int i = 0; i < subjectCombo.getItemCount(); i++) {
                if (subjectCombo.getItemAt(i).id == record.getSubjectId()) {
                    subjectCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void loadSubjects() {
        SubjectService ss = new SubjectService();
        List<Subject> subjects = ss.getAllSubjects();
        for (Subject s : subjects) {
            subjectCombo.addItem(new SubjectItem(s.getId(), s.getSubjectName()));
        }
    }
    
    private void save() {
        try {
            AttendanceRecord a = record == null ? new AttendanceRecord() : record;
            a.setStudentId(studentIdField.getText().trim());
            
            SubjectItem selSubj = (SubjectItem) subjectCombo.getSelectedItem();
            if (selSubj != null) a.setSubjectId(selSubj.id);
            
            a.setAttendanceDate(Date.valueOf(dateField.getText().trim()));
            a.setStatus((String) statusCombo.getSelectedItem());
            
            if (record == null) {
                service.addRecord(a);
            } else {
                service.updateRecord(a);
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
    
    private static class SubjectItem {
        int id;
        String name;
        SubjectItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() { return name; }
    }
}
