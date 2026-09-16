package com.uniportal.ui.auth;

import com.uniportal.Main;
import com.uniportal.service.AuthService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;

    public LoginFrame() {
        this.authService = new AuthService();
        setLayout(new GridLayout(1, 2));
        setBackground(UIUtils.COLOR_BACKGROUND);

        // Left Panel (Banner)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(93, 95, 239), getWidth(), getHeight(), new Color(133, 102, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        leftPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 40, 10, 40);
        gbcLeft.anchor = GridBagConstraints.WEST;
        
        JLabel mainTitle = new JLabel("Big Dreams");
        mainTitle.setFont(new Font("Inter", Font.BOLD, 48));
        mainTitle.setForeground(Color.WHITE);
        leftPanel.add(mainTitle, gbcLeft);
        
        gbcLeft.gridy = 1;
        JLabel subTitle = new JLabel("Brighter Tomorrows");
        subTitle.setFont(new Font("Inter", Font.BOLD, 48));
        subTitle.setForeground(Color.WHITE);
        leftPanel.add(subTitle, gbcLeft);

        gbcLeft.gridy = 2;
        gbcLeft.insets = new Insets(30, 40, 10, 40);
        JLabel textDesc = new JLabel("<html>Learn • Connect • Grow<br>at UniPortal</html>");
        textDesc.setFont(new Font("Inter", Font.PLAIN, 20));
        textDesc.setForeground(new Color(255, 255, 255, 200));
        leftPanel.add(textDesc, gbcLeft);

        add(leftPanel);

        // Right Panel (Form)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.putClientProperty("FlatLaf.style", "arc: 30"); // FlatLaf rounded panel
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        // Logo / Title
        JLabel titleLabel = new JLabel("UniPortal");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(UIUtils.COLOR_SIDEBAR);
        titleLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon")); // Placeholder icon
        formPanel.add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Your Campus. One Portal.");
        subtitleLabel.setFont(UIUtils.FONT_NORMAL);
        subtitleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10);
        formPanel.add(subtitleLabel, gbc);
        
        // Input Fields
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.gridy = 2;
        usernameField = new JTextField(25);
        usernameField.putClientProperty("JTextField.placeholderText", "Student ID / Enrollment No.");
        usernameField.setPreferredSize(new Dimension(300, 45));
        usernameField.setFont(UIUtils.FONT_NORMAL);
        formPanel.add(usernameField, gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField(25);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.putClientProperty("JTextField.showRevealButton", true);
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setFont(UIUtils.FONT_NORMAL);
        formPanel.add(passwordField, gbc);
        
        // Sign In Button
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        StyledButton loginBtn = new StyledButton("Sign In \u2192");
        loginBtn.setPreferredSize(new Dimension(300, 45));
        loginBtn.addActionListener(e -> performLogin());
        formPanel.add(loginBtn, gbc);
        
        // Links
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel linkLabel = new JLabel("<html><a href='#'>New here? Contact Administrator</a></html>");
        linkLabel.setFont(UIUtils.FONT_SMALL);
        linkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(linkLabel, gbc);

        rightPanel.add(formPanel);
        add(rightPanel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            if (authService.login(username, password)) {
                // Remove redundant dialog
                Main.getMainFrame().refreshUserInfo();
                
                String role = com.uniportal.util.SessionManager.getCurrentUser().getRole();
                if ("ADMIN".equals(role)) {
                    Main.getMainFrame().showPanel("AdminDashboard", "Admin Dashboard");
                } else if ("STUDENT".equals(role)) {
                    Main.getMainFrame().showPanel("StudentDashboard", "Good Morning, " + com.uniportal.util.SessionManager.getCurrentUser().getUsername() + "!");
                }
                
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}
