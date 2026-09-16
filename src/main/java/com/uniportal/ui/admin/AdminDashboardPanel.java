package com.uniportal.ui.admin;

import com.uniportal.ui.common.DashboardCard;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Center Content Area
        JPanel centerContent = new JPanel(new BorderLayout(20, 20));
        centerContent.setOpaque(false);

        // Cards Panel (2x4 Grid)
        JPanel cardsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        cardsPanel.setOpaque(false);
        
        cardsPanel.add(createSummaryCard("Total Students", "1240", new Color(13, 110, 253)));
        cardsPanel.add(createSummaryCard("Total Faculty", "86", new Color(40, 167, 69)));
        cardsPanel.add(createSummaryCard("Departments", "8", new Color(111, 66, 193)));
        cardsPanel.add(createSummaryCard("Total Notices", "18", new Color(253, 126, 20)));
        
        cardsPanel.add(createSummaryCard("Assignments", "42", new Color(23, 162, 184)));
        cardsPanel.add(createSummaryCard("Events", "12", new Color(220, 53, 69)));
        cardsPanel.add(createSummaryCard("Open Tickets", "17", new Color(255, 193, 7)));
        cardsPanel.add(createSummaryCard("Avg Attendance", "82%", new Color(32, 201, 151)));
        
        centerContent.add(cardsPanel, BorderLayout.NORTH);
        
        // Placeholder for charts or activity feed in the center
        DashboardCard activityCard = new DashboardCard();
        activityCard.setLayout(new BorderLayout());
        activityCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel aTitle = new JLabel("Recent System Activity");
        aTitle.setFont(UIUtils.FONT_HEADER);
        activityCard.add(aTitle, BorderLayout.NORTH);
        
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityList.setBorder(new EmptyBorder(10, 0, 0, 0));
        activityList.add(createActivityItem("New Notice Published: Mid-Semester Exams", "2 hours ago"));
        activityList.add(Box.createRigidArea(new Dimension(0, 10)));
        activityList.add(createActivityItem("New Faculty Registered: Dr. Smith", "5 hours ago"));
        activityList.add(Box.createRigidArea(new Dimension(0, 10)));
        activityList.add(createActivityItem("Timetable updated for CS Dept", "1 day ago"));
        activityCard.add(activityList, BorderLayout.CENTER);
        
        centerContent.add(activityCard, BorderLayout.CENTER);
        
        add(centerContent, BorderLayout.CENTER);

        // Right Side Banner Area
        JPanel rightBannerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(93, 95, 239), 0, getHeight(), new Color(63, 61, 150));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        rightBannerPanel.setPreferredSize(new Dimension(250, 0));
        rightBannerPanel.setLayout(new GridBagLayout());
        
        JLabel quoteLabel = new JLabel("<html><div style='text-align: center;'>Administer<br>with<br>Excellence.</div></html>");
        quoteLabel.setFont(new Font("Inter", Font.BOLD, 24));
        quoteLabel.setForeground(Color.WHITE);
        rightBannerPanel.add(quoteLabel);

        add(rightBannerPanel, BorderLayout.EAST);
    }
    
    private DashboardCard createSummaryCard(String title, String value, Color valueColor) {
        DashboardCard card = new DashboardCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 32));
        valueLabel.setForeground(valueColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.FONT_SMALL);
        titleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        
        return card;
    }
    
    private JPanel createActivityItem(String text, String time) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel dotLabel = new JLabel("•");
        dotLabel.setFont(new Font("Inter", Font.BOLD, 24));
        dotLabel.setForeground(new Color(93, 95, 239));
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(UIUtils.FONT_NORMAL);
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(UIUtils.FONT_SMALL);
        timeLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(textLabel);
        detailPanel.add(timeLabel);
        
        panel.add(dotLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        return panel;
    }
}
