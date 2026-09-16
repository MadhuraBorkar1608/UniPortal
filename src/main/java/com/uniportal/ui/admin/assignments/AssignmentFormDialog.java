package com.uniportal.ui.admin.assignments;

import com.uniportal.model.Assignment;
import com.uniportal.model.Subject;
import com.uniportal.service.AssignmentService;
import com.uniportal.service.SubjectService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class AssignmentFormDialog extends JDialog {

    private JTextField titleField;
    private JComboBox<SubjectItem> subjectCombo;
    private JTextArea descArea;
    private JTextField deadlineField;
    private JTextField attachmentField;
    
    private Assignment assignment;
    private AssignmentService service;
    private boolean saved = false;

    public AssignmentFormDialog(Window owner, Assignment assignment, AssignmentService service) {
        super(owner, assignment == null ? "Add Assignment" : "Edit Assignment", ModalityType.APPLICATION_MODAL);
        this.assignment = assignment;
        this.service = service;
        
        setSize(450, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(titleField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Subject:"), gbc);
        subjectCombo = new JComboBox<>();
        loadSubjects();
        gbc.gridx = 1; formPanel.add(subjectCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        gbc.gridx = 1; formPanel.add(new JScrollPane(descArea), gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Deadline (YYYY-MM-DD HH:MM:SS):"), gbc);
        deadlineField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(deadlineField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Attachment Path:"), gbc);
        attachmentField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(attachmentField, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (assignment != null) {
            titleField.setText(assignment.getTitle());
            descArea.setText(assignment.getDescription());
            deadlineField.setText(assignment.getDeadline().toString());
            attachmentField.setText(assignment.getAttachmentPath());
            
            for (int i = 0; i < subjectCombo.getItemCount(); i++) {
                if (subjectCombo.getItemAt(i).id == assignment.getSubjectId()) {
                    subjectCombo.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            deadlineField.setText(new Timestamp(System.currentTimeMillis() + 7L * 24 * 3600 * 1000).toString());
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
            Assignment a = assignment == null ? new Assignment() : assignment;
            a.setTitle(titleField.getText().trim());
            
            SubjectItem selSubj = (SubjectItem) subjectCombo.getSelectedItem();
            if (selSubj != null) a.setSubjectId(selSubj.id);
            
            a.setDescription(descArea.getText().trim());
            a.setDeadline(Timestamp.valueOf(deadlineField.getText().trim()));
            a.setAttachmentPath(attachmentField.getText().trim());
            
            if (assignment == null) {
                service.addAssignment(a);
            } else {
                service.updateAssignment(a);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            UIUtils.showError(this, "Invalid date/time format. Use YYYY-MM-DD HH:MM:SS");
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
