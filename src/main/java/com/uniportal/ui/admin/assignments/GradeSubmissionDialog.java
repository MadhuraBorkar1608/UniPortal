package com.uniportal.ui.admin.assignments;

import com.uniportal.model.AssignmentSubmission;
import com.uniportal.service.AssignmentSubmissionService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;

public class GradeSubmissionDialog extends JDialog {

    private JComboBox<String> statusCombo;
    private JTextField gradeField;
    private JTextArea feedbackArea;
    
    private AssignmentSubmission submission;
    private AssignmentSubmissionService service;
    private boolean saved = false;

    public GradeSubmissionDialog(Window owner, AssignmentSubmission submission, AssignmentSubmissionService service) {
        super(owner, "Grade Submission", ModalityType.APPLICATION_MODAL);
        this.submission = submission;
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
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1; formPanel.add(new JLabel(submission.getStudentName() + " (" + submission.getStudentId() + ")"), gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("File Path:"), gbc);
        gbc.gridx = 1; formPanel.add(new JLabel(submission.getSubmissionPath()), gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"SUBMITTED", "LATE", "GRADED"});
        statusCombo.setSelectedItem(submission.getStatus() != null ? submission.getStatus() : "GRADED");
        gbc.gridx = 1; formPanel.add(statusCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Grade:"), gbc);
        gradeField = new JTextField(20);
        gradeField.setText(submission.getGrade());
        gbc.gridx = 1; formPanel.add(gradeField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Feedback:"), gbc);
        feedbackArea = new JTextArea(4, 20);
        feedbackArea.setLineWrap(true);
        feedbackArea.setText(submission.getFeedback());
        gbc.gridx = 1; formPanel.add(new JScrollPane(feedbackArea), gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save Grade");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void save() {
        try {
            String status = (String) statusCombo.getSelectedItem();
            String grade = gradeField.getText().trim();
            String feedback = feedbackArea.getText().trim();
            
            service.gradeSubmission(submission.getId(), status, grade, feedback);
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
