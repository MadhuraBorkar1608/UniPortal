package com.uniportal.ui.auth;

import com.uniportal.Main;
import com.uniportal.service.AuthService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;
    private JToggleButton adminToggle;
    private JToggleButton studentToggle;

    public LoginFrame() {
        this.authService = new AuthService();
        setLayout(new GridBagLayout()); // Centers the content
        setBackground(new Color(248, 246, 240)); // Warm off-white background

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);
        GridBagConstraints gbcContainer = new GridBagConstraints();
        gbcContainer.gridx = 0; 
        gbcContainer.gridy = 0;
        gbcContainer.insets = new Insets(10, 10, 30, 10);
        gbcContainer.anchor = GridBagConstraints.CENTER;

        // App Name above the form
        JLabel appTitle = new JLabel("UniPortal");
        appTitle.setFont(new Font("Inter", Font.BOLD, 48));
        appTitle.setForeground(UIUtils.COLOR_PRIMARY);
        centerContainer.add(appTitle, gbcContainer);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.putClientProperty("FlatLaf.style", "arc: 20");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(UIUtils.COLOR_SIDEBAR);
        formPanel.add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Sign in to continue to UniPortal");
        subtitleLabel.setFont(UIUtils.FONT_NORMAL);
        subtitleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 25, 10);
        formPanel.add(subtitleLabel, gbc);
        
        // Toggle Panel
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        JPanel togglePanel = new JPanel(new GridLayout(1, 2));
        togglePanel.setBackground(new Color(230, 235, 240));
        
        adminToggle = new JToggleButton("Admin");
        adminToggle.setFont(UIUtils.FONT_NORMAL);
        adminToggle.setFocusPainted(false);
        adminToggle.setBackground(new Color(230, 235, 240));
        adminToggle.setForeground(Color.BLACK);
        
        studentToggle = new JToggleButton("Student");
        studentToggle.setFont(UIUtils.FONT_NORMAL);
        studentToggle.setFocusPainted(false);
        studentToggle.setBackground(new Color(10, 35, 66));
        studentToggle.setForeground(Color.WHITE);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(adminToggle);
        bg.add(studentToggle);
        studentToggle.setSelected(true); // default
        
        ActionListener toggleListener = e -> {
            if (adminToggle.isSelected()) {
                adminToggle.setBackground(new Color(10, 35, 66));
                adminToggle.setForeground(Color.WHITE);
                studentToggle.setBackground(new Color(230, 235, 240));
                studentToggle.setForeground(Color.BLACK);
            } else {
                studentToggle.setBackground(new Color(10, 35, 66));
                studentToggle.setForeground(Color.WHITE);
                adminToggle.setBackground(new Color(230, 235, 240));
                adminToggle.setForeground(Color.BLACK);
            }
        };
        adminToggle.addActionListener(toggleListener);
        studentToggle.addActionListener(toggleListener);
        
        togglePanel.add(adminToggle);
        togglePanel.add(studentToggle);
        togglePanel.setPreferredSize(new Dimension(300, 45));
        formPanel.add(togglePanel, gbc);
        
        // Input Fields
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.gridy = 3;
        usernameField = new JTextField(25);
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        usernameField.setPreferredSize(new Dimension(300, 45));
        usernameField.setFont(UIUtils.FONT_NORMAL);
        formPanel.add(usernameField, gbc);

        gbc.gridy = 4;
        passwordField = new JPasswordField(25);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.putClientProperty("JTextField.showRevealButton", true);
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setFont(UIUtils.FONT_NORMAL);
        formPanel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 20, 10);
        StyledButton loginBtn = new StyledButton("Login");
        loginBtn.setPreferredSize(new Dimension(300, 45));
        loginBtn.addActionListener(e -> performLogin());
        formPanel.add(loginBtn, gbc);
        
        // Links
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel linkLabel = new JLabel("<html>Don't have an account? <a href='#'>Contact Admin</a></html>");
        linkLabel.setFont(UIUtils.FONT_SMALL);
        linkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(linkLabel, gbc);

        gbcContainer.gridy = 1;
        gbcContainer.insets = new Insets(0, 0, 0, 0);
        centerContainer.add(formPanel, gbcContainer);
        
        add(centerContainer);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            if (authService.login(username, password)) {
                Main.getMainFrame().refreshUserInfo();
                
                String role = com.uniportal.util.SessionManager.getCurrentUser().getRole();
                if ("ADMIN".equals(role)) {
                    Main.getMainFrame().showPanel("AdminDashboard", "Admin Dashboard");
                } else if ("STUDENT".equals(role)) {
                    Main.getMainFrame().showPanel("StudentDashboard", "Good Morning, " + Main.getMainFrame().getCurrentUserDisplayName() + "!");
                }
            }
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}
