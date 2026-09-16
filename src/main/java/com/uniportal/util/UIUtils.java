package com.uniportal.util;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

public class UIUtils {

    public static void setGlobalFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    // Standard Colors (Modern UI)
    public static final Color COLOR_PRIMARY = new Color(93, 95, 239); // Vibrant Purple/Blue
    public static final Color COLOR_BACKGROUND = new Color(248, 249, 253); // Light grayish blue
    public static final Color COLOR_SIDEBAR = new Color(26, 29, 45); // Very dark blue
    public static final Color COLOR_SIDEBAR_HOVER = new Color(52, 56, 84); // Lighter dark blue
    public static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255);
    public static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    public static final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    
    // Status Colors
    public static final Color COLOR_SUCCESS = new Color(40, 167, 69);
    public static final Color COLOR_WARNING = new Color(255, 193, 7);
    public static final Color COLOR_INFO = new Color(23, 162, 184);
    
    // Standard Fonts
    public static final Font FONT_TITLE = new Font("Inter", Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font("Inter", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("Inter", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Inter", Font.PLAIN, 12);
    
    public static void setupCustomUI() {
        // FlatLaf rounded corners
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("ProgressBar.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        
        // Custom colors
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.focusColor", COLOR_PRIMARY);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        
        // Remove table grid lines
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("TableHeader.background", Color.WHITE);
        UIManager.put("TableHeader.separatorColor", new Color(230, 230, 230));
        UIManager.put("TableHeader.bottomSeparatorColor", new Color(200, 200, 200));
        UIManager.put("Table.selectionBackground", new Color(240, 240, 245));
        UIManager.put("Table.selectionForeground", COLOR_TEXT_DARK);
        UIManager.put("Table.alternateRowColor", new Color(250, 250, 250));
    }
    
    public static void styleTable(javax.swing.JTable table) {
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));
        table.getTableHeader().setFont(FONT_NORMAL.deriveFont(java.awt.Font.BOLD));
        table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }
}
