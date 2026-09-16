package com.uniportal.ui.common;

import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class HeaderPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel roleLabel;
    private JTextField searchField;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230))); // Bottom border

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(UIUtils.FONT_TITLE.deriveFont(22f));
        titleLabel.setForeground(UIUtils.COLOR_SIDEBAR);
        titleLabel.setBorder(new EmptyBorder(15, 30, 15, 20));

        // Right side container
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setOpaque(false);
        
        // Search bar
        searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Search anything...");
        searchField.putClientProperty("JTextField.showClearButton", true);
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setFont(UIUtils.FONT_NORMAL);
        rightPanel.add(searchField);
        
        // Notification Bell
        JLabel notifLabel = new JLabel("🔔");
        notifLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        notifLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(notifLabel);

        // User info panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        
        userLabel = new JLabel("");
        userLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        userLabel.setForeground(UIUtils.COLOR_SIDEBAR);
        userLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        roleLabel = new JLabel("");
        roleLabel.setFont(UIUtils.FONT_SMALL);
        roleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        roleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(roleLabel);
        
        rightPanel.add(userInfoPanel);
        
        // User Avatar (Placeholder)
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        rightPanel.add(avatarLabel);
        
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        add(titleLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setUserInfo(String name, String role) {
        userLabel.setText(name);
        roleLabel.setText(role);
    }
}
