package com.uniportal.ui.common;

import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class SidebarPanel extends JPanel {

    private JPanel buttonPanel;
    private Map<String, JToggleButton> buttons;
    private ButtonGroup buttonGroup;

    public SidebarPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_SIDEBAR);
        setPreferredSize(new Dimension(280, 0));

        // Logo / Title area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 30));
        logoPanel.setOpaque(false);
        JLabel logoIcon = new JLabel(); 
        try {
            java.net.URL imgUrl = getClass().getResource("/logo.png");
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image img = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                logoIcon.setIcon(new ImageIcon(img));
            } else {
                logoIcon.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            }
        } catch (Exception e) {
            logoIcon.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        }
        JLabel logoLabel = new JLabel("UniPortal");
        logoLabel.setFont(UIUtils.FONT_TITLE.deriveFont(22f));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoIcon);
        logoPanel.add(logoLabel);
        logoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(logoPanel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().putClientProperty("JScrollBar.showButtons", false);
        add(scrollPane, BorderLayout.CENTER);

        buttons = new LinkedHashMap<>();
        buttonGroup = new ButtonGroup();
        
        // Bottom Quote
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        JLabel quote = new JLabel("<html>A Better You<br>A Brighter Tomorrow</html>");
        quote.setForeground(new Color(255, 255, 255, 180));
        quote.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.ITALIC));
        bottomPanel.add(quote, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addMenuButton(String label, String actionCommand, ActionListener listener) {
        JToggleButton btn = new JToggleButton("  " + label);
        btn.setFont(UIUtils.FONT_NORMAL.deriveFont(15f));
        btn.setForeground(new Color(220, 225, 235));
        btn.setBackground(UIUtils.COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setActionCommand(actionCommand);
        
        // FlatLaf toggle button styling
        btn.putClientProperty("JToggleButton.buttonType", "roundRect");
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("JButton.selectedBackground", UIUtils.COLOR_PRIMARY);
        btn.putClientProperty("JButton.selectedForeground", Color.WHITE);
        btn.putClientProperty("JButton.hoverBackground", UIUtils.COLOR_SIDEBAR_HOVER);

        btn.addActionListener(e -> {
            listener.actionPerformed(e);
            // Re-assert selection visually
            for (JToggleButton tb : buttons.values()) {
                tb.setOpaque(tb.isSelected());
            }
            btn.setOpaque(true);
        });

        buttonGroup.add(btn);
        buttons.put(actionCommand, btn);
        buttonPanel.add(btn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    public void setMenuStateForRole(String role) {
        buttonPanel.removeAll();
        buttons.clear();
        // Remove existing from group
        while (buttonGroup.getButtonCount() > 0) {
            buttonGroup.remove(buttonGroup.getElements().nextElement());
        }
    }
    
    public void selectButton(String actionCommand) {
        if (buttons.containsKey(actionCommand)) {
            buttons.get(actionCommand).setSelected(true);
            buttons.get(actionCommand).setOpaque(true);
        }
    }
}
