package com.uniportal.ui.admin;

import com.uniportal.model.Admin;
import com.uniportal.service.AdminService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminProfilePanel extends JPanel {

    private AdminService adminService;
    private Admin currentAdmin;

    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextField designationField;

    public AdminProfilePanel() {
        adminService = new AdminService();
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
        currentAdmin = adminService.getAdminProfile(SessionManager.getCurrentUser().getId());
        
        if (currentAdmin == null) {
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
        addReadOnlyField(formPanel, gbc, row++, "Admin ID:", currentAdmin.getAdminId());
        
        fullNameField = addEditableField(formPanel, gbc, row++, "Full Name:", currentAdmin.getFullName());
        addReadOnlyField(formPanel, gbc, row++, "Email:", currentAdmin.getEmail());
        phoneField = addEditableField(formPanel, gbc, row++, "Phone:", currentAdmin.getPhone());
        designationField = addEditableField(formPanel, gbc, row++, "Designation:", currentAdmin.getDesignation());
        
        StyledButton saveBtn = new StyledButton("Save Changes");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(saveBtn, gbc);

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
            currentAdmin.setFullName(fullNameField.getText().trim());
            currentAdmin.setPhone(phoneField.getText().trim());
            currentAdmin.setDesignation(designationField.getText().trim());
            
            adminService.updateAdmin(currentAdmin);
            UIUtils.showSuccess(this, "Profile updated successfully!");
            loadProfile();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}
