package com.uniportal.ui.student;

import com.uniportal.ui.common.DashboardCard;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class StudentDashboardPanel extends JPanel {

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Center Content Area (Cards + Bottom content)
        JPanel centerContent = new JPanel(new BorderLayout(20, 20));
        centerContent.setOpaque(false);

        // Top Summary Cards
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        topCardsPanel.setOpaque(false);
        
        topCardsPanel.add(createSummaryCard("Attendance", "75%", new Color(40, 167, 69))); // Green
        topCardsPanel.add(createSummaryCard("Pending Assignments", "3", new Color(253, 126, 20))); // Orange
        topCardsPanel.add(createSummaryCard("Upcoming Exams", "2", new Color(13, 110, 253))); // Blue
        topCardsPanel.add(createSummaryCard("Registered Events", "4", new Color(111, 66, 193))); // Purple
        
        centerContent.add(topCardsPanel, BorderLayout.NORTH);
        
        // Bottom Content Area (Timetable + Notices)
        JPanel bottomContent = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomContent.setOpaque(false);
        
        DashboardCard timetableCard = new DashboardCard();
        timetableCard.setLayout(new BorderLayout());
        timetableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel tTitle = new JLabel("Today's Schedule");
        tTitle.setFont(UIUtils.FONT_HEADER);
        timetableCard.add(tTitle, BorderLayout.NORTH);
        
        // Dummy schedule data
        JPanel scheduleList = new JPanel();
        scheduleList.setLayout(new BoxLayout(scheduleList, BoxLayout.Y_AXIS));
        scheduleList.setOpaque(false);
        scheduleList.setBorder(new EmptyBorder(10, 0, 0, 0));
        scheduleList.add(createScheduleItem("09:00 AM", "Data Structures", "Room A-101"));
        scheduleList.add(Box.createRigidArea(new Dimension(0, 10)));
        scheduleList.add(createScheduleItem("11:00 AM", "Database Management", "Room A-102"));
        scheduleList.add(Box.createRigidArea(new Dimension(0, 10)));
        scheduleList.add(createScheduleItem("01:00 PM", "Operating Systems", "Room A-103"));
        timetableCard.add(scheduleList, BorderLayout.CENTER);
        
        DashboardCard noticeCard = new DashboardCard();
        noticeCard.setLayout(new BorderLayout());
        noticeCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel nTitle = new JLabel("Recent Notices");
        nTitle.setFont(UIUtils.FONT_HEADER);
        noticeCard.add(nTitle, BorderLayout.NORTH);
        
        // Dummy notice data
        JPanel noticeList = new JPanel();
        noticeList.setLayout(new BoxLayout(noticeList, BoxLayout.Y_AXIS));
        noticeList.setOpaque(false);
        noticeList.setBorder(new EmptyBorder(10, 0, 0, 0));
        noticeList.add(createNoticeItem("Mid-Semester Exam Schedule Released", "Sep 5, 2026", new Color(253, 126, 20)));
        noticeList.add(Box.createRigidArea(new Dimension(0, 10)));
        noticeList.add(createNoticeItem("College Cultural Fest Registration Open", "Sep 4, 2026", new Color(220, 53, 69)));
        noticeList.add(Box.createRigidArea(new Dimension(0, 10)));
        noticeList.add(createNoticeItem("Library Timings Updated", "Sep 3, 2026", new Color(111, 66, 193)));
        noticeCard.add(noticeList, BorderLayout.CENTER);
        
        bottomContent.add(timetableCard);
        bottomContent.add(noticeCard);
        
        centerContent.add(bottomContent, BorderLayout.CENTER);
        
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
        
        JLabel quoteLabel = new JLabel("<html><div style='text-align: center;'>Discipline<br>today builds<br>the success<br>of tomorrow.</div></html>");
        quoteLabel.setFont(new Font("Inter", Font.BOLD, 22));
        quoteLabel.setForeground(Color.WHITE);
        rightBannerPanel.add(quoteLabel);

        add(rightBannerPanel, BorderLayout.EAST);
    }
    
    private DashboardCard createSummaryCard(String title, String value, Color valueColor) {
        DashboardCard card = new DashboardCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 20, 25, 20));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 36));
        valueLabel.setForeground(valueColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("<html>" + title.replace(" ", "<br>") + "</html>");
        titleLabel.setFont(UIUtils.FONT_SMALL);
        titleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        
        return card;
    }
    
    private JPanel createScheduleItem(String time, String subject, String room) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel timeLabel = new JLabel("🕒 " + time);
        timeLabel.setFont(UIUtils.FONT_SMALL);
        timeLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel subLabel = new JLabel(subject);
        subLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        JLabel roomLabel = new JLabel(room);
        roomLabel.setFont(UIUtils.FONT_SMALL);
        roomLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(subLabel);
        detailPanel.add(roomLabel);
        
        panel.add(timeLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createNoticeItem(String title, String date, Color iconColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📄");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(iconColor); // Emoji might not tint, but good intent
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(UIUtils.FONT_SMALL);
        dateLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(titleLabel);
        detailPanel.add(dateLabel);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        return panel;
    }
}
