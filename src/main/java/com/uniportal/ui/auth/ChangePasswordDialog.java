package com.uniportal.ui.auth;

import com.uniportal.service.AuthService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private AuthService authService;

    public ChangePasswordDialog(JFrame parent) {
        super(parent, "Change Password", true);
        this.authService = new AuthService();
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Current Password:"));
        currentPasswordField = new JPasswordField();
        formPanel.add(currentPasswordField);

        formPanel.add(new JLabel("New Password:"));
        newPasswordField = new JPasswordField();
        formPanel.add(newPasswordField);

        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void changePassword() {
        try {
            authService.changePassword(
                new String(currentPasswordField.getPassword()),
                new String(newPasswordField.getPassword()),
                new String(confirmPasswordField.getPassword())
            );
            UIUtils.showSuccess(this, "Password changed successfully.");
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}
