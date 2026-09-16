package com.uniportal.ui.common;

import com.uniportal.util.UIUtils;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Dimension;

public class StyledButton extends JButton {
    public StyledButton(String text) {
        super(text);
        setFont(UIUtils.FONT_NORMAL);
        setBackground(UIUtils.COLOR_PRIMARY);
        setForeground(UIUtils.COLOR_TEXT_LIGHT);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(150, 40));
    }
}
