package com.uniportal.ui.admin.assignments;

import com.uniportal.model.Assignment;
import com.uniportal.model.AssignmentSubmission;
import com.uniportal.service.AssignmentSubmissionService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.io.File;

public class StudentSubmitDialog extends JDialog {

    private Assignment assignment;
    private String studentId;
    private AssignmentSubmissionService service;
    private JTextField fileField;
    private boolean submitted = false;

    public StudentSubmitDialog(Window owner, Assignment assignment, String studentId) {
        super(owner, "Submit Assignment", ModalityType.APPLICATION_MODAL);
        this.assignment = assignment;
        this.studentId = studentId;
        this.service = new AssignmentSubmissionService();
        
        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Assignment:"), gbc);
        gbc.gridx = 1; formPanel.add(new JLabel(assignment.getTitle()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("File:"), gbc);
        
        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        fileField = new JTextField(15);
        fileField.setEditable(false);
        StyledButton browseBtn = new StyledButton("Browse");
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                fileField.setText(f.getAbsolutePath());
            }
        });
        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(browseBtn, BorderLayout.EAST);
        
        gbc.gridx = 1; formPanel.add(filePanel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton submitBtn = new StyledButton("Submit");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        submitBtn.addActionListener(e -> submit());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void submit() {
        if (fileField.getText().isEmpty()) {
            UIUtils.showError(this, "Please select a file.");
            return;
        }
        try {
            AssignmentSubmission sub = new AssignmentSubmission();
            sub.setAssignmentId(assignment.getId());
            sub.setStudentId(studentId);
            sub.setSubmissionPath(fileField.getText());
            sub.setSubmissionTime(new Timestamp(System.currentTimeMillis()));
            
            if (sub.getSubmissionTime().after(assignment.getDeadline())) {
                sub.setStatus("LATE");
            } else {
                sub.setStatus("SUBMITTED");
            }
            
            service.addSubmission(sub);
            submitted = true;
            UIUtils.showSuccess(this, "Assignment submitted.");
            dispose();
        } catch (Exception e) {
            UIUtils.showError(this, "Error submitting: " + e.getMessage());
        }
    }
    
    public boolean isSubmitted() {
        return submitted;
    }
}
