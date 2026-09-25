package com.uniportal.ui.student;

import com.uniportal.model.Student;
import com.uniportal.service.StudentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentProfilePanel extends JPanel {

    private StudentService studentService;
    private Student currentStudent;

    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;

    public StudentProfilePanel() {
        studentService = new StudentService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        loadProfile();
    }

    public void loadProfile() {
        removeAll();
        if (!SessionManager.isAuthenticated()) {
            return;
        }
        currentStudent = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
        
        if (currentStudent == null) {
            add(new JLabel("Profile information is currently unavailable."), BorderLayout.CENTER);
            return;
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        addReadOnlyField(formPanel, gbc, row++, "Student ID:", currentStudent.getStudentId());
        addReadOnlyField(formPanel, gbc, row++, "Full Name:", currentStudent.getFullName());
        
        emailField = addEditableField(formPanel, gbc, row++, "Email:", currentStudent.getEmail());
        phoneField = addEditableField(formPanel, gbc, row++, "Phone:", currentStudent.getPhone());
        
        addReadOnlyField(formPanel, gbc, row++, "Department:", currentStudent.getDeptName());
        addReadOnlyField(formPanel, gbc, row++, "Course:", currentStudent.getCourseName());
        addReadOnlyField(formPanel, gbc, row++, "Semester:", String.valueOf(currentStudent.getCurrentSemester()));
        addReadOnlyField(formPanel, gbc, row++, "Division:", currentStudent.getDivision());
        
        addressField = addEditableField(formPanel, gbc, row++, "Address:", currentStudent.getAddress());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        StyledButton changePasswordBtn = new StyledButton("Change Password");
        changePasswordBtn.setBackground(new Color(108, 117, 125));
        changePasswordBtn.addActionListener(e -> new com.uniportal.ui.auth.ChangePasswordDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true));

        StyledButton logoutBtn = new StyledButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.addActionListener(e -> {
            com.uniportal.util.SessionManager.logout();
            com.uniportal.Main.getMainFrame().showPanel("Login", "Login");
            com.uniportal.Main.getMainFrame().refreshUserInfo();
        });

        StyledButton saveBtn = new StyledButton("Save Changes");
        saveBtn.addActionListener(e -> saveProfile());
        
        buttonPanel.add(changePasswordBtn);
        buttonPanel.add(logoutBtn);
        buttonPanel.add(saveBtn);
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel);

        add(centerPanel, BorderLayout.NORTH);
        
        revalidate();
        repaint();
    }
    
    private void addReadOnlyField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String value) {
        gbc.gridy = row;
        
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.FONT_HEADER);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JLabel valLabel = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        valLabel.setFont(UIUtils.FONT_NORMAL);
        panel.add(valLabel, gbc);
    }
    
    private JTextField addEditableField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String value) {
        gbc.gridy = row;
        
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.FONT_HEADER);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField textField = new JTextField(20);
        textField.setText(value != null ? value : "");
        textField.setFont(UIUtils.FONT_NORMAL);
        panel.add(textField, gbc);
        
        return textField;
    }
    
    private void saveProfile() {
        try {
            currentStudent.setEmail(emailField.getText().trim());
            currentStudent.setPhone(phoneField.getText().trim());
            currentStudent.setAddress(addressField.getText().trim());
            
            studentService.updateStudent(currentStudent);
            UIUtils.showSuccess(this, "Profile updated successfully!");
            loadProfile();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}
