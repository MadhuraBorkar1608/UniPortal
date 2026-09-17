package com.uniportal.ui.common;

import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class HeaderPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel roleLabel;
    private JPanel rightPanel;
    private JLabel notifLabel;
    private JLabel avatarLabel;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230))); // Bottom border

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(UIUtils.FONT_TITLE.deriveFont(22f));
        titleLabel.setForeground(UIUtils.COLOR_SIDEBAR);
        titleLabel.setBorder(new EmptyBorder(15, 30, 15, 20));

        // Right side container
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setOpaque(false);
        
        // Notification Bell
        notifLabel = new JLabel("🔔");
        notifLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        notifLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notifLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    String role = com.uniportal.util.SessionManager.getCurrentUser().getRole();
                    if ("ADMIN".equals(role)) {
                        com.uniportal.Main.getMainFrame().showPanel("ManageNotices", "Manage Notices");
                    } else if ("STUDENT".equals(role)) {
                        com.uniportal.Main.getMainFrame().showPanel("StudentNotices", "Notices");
                    }
                } catch (Exception ex) {}
            }
        });
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
        
        // User Avatar
        avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatarLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    String role = com.uniportal.util.SessionManager.getCurrentUser().getRole();
                    javax.swing.JPanel contentPanel = (javax.swing.JPanel) com.uniportal.Main.getMainFrame().getContentPane().getComponent(1);
                    if ("ADMIN".equals(role)) {
                        com.uniportal.Main.getMainFrame().showPanel("AdminProfile", "My Profile");
                    } else if ("STUDENT".equals(role)) {
                        // find StudentProfilePanel and call loadProfile
                        for (Component comp : com.uniportal.Main.getMainFrame().getContentPane().getComponents()) {
                            // actually it's inside the right panel, let's just find it
                        }
                        com.uniportal.Main.getMainFrame().showPanel("StudentProfile", "My Profile");
                    }
                } catch (Exception ex) {}
            }
        });
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

    public void setIconsVisible(boolean visible) {
        rightPanel.setVisible(visible);
    }
}
