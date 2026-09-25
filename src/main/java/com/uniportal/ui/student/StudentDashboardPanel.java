package com.uniportal.ui.student;

import com.uniportal.ui.common.DashboardCard;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import com.uniportal.service.NoticeService;
import com.uniportal.model.Notice;
import com.uniportal.service.TimetableService;
import com.uniportal.model.Timetable;
import com.uniportal.service.AssignmentService;
import com.uniportal.service.EventService;
import com.uniportal.model.CalendarEvent;
import java.util.List;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        String assignmentsCount = "0";
        String eventsCount = "0";
        
        try {
            assignmentsCount = String.valueOf(new AssignmentService().getAssignmentsForStudent(1, 1).size());
            eventsCount = String.valueOf(new EventService().getAllEvents().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        topCardsPanel.add(createSummaryCard("Assignments", assignmentsCount, new Color(13, 110, 253))); // Blue
        topCardsPanel.add(createSummaryCard("Registered Events", eventsCount, new Color(111, 66, 193))); // Purple
        
        centerContent.add(topCardsPanel, BorderLayout.NORTH);
        
        // Bottom Content Area (Timetable + Calendar + Notices)
        JPanel bottomContent = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomContent.setOpaque(false);
        
        DashboardCard timetableCard = new DashboardCard();
        timetableCard.setLayout(new BorderLayout());
        timetableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel timetableTop = new JPanel(new BorderLayout());
        timetableTop.setOpaque(false);
        
        JLabel tTitle = new JLabel("Today's Schedule");
        tTitle.setFont(UIUtils.FONT_HEADER);
        timetableTop.add(tTitle, BorderLayout.NORTH);
        
        JPanel scheduleList = new JPanel();
        scheduleList.setLayout(new BoxLayout(scheduleList, BoxLayout.Y_AXIS));
        scheduleList.setOpaque(false);
        scheduleList.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        try {
            TimetableService timetableService = new TimetableService();
            // Fetching a dummy division/dept since we don't have current student context here easily
            List<Timetable> scheduleListItems = timetableService.getStudentTimetable("A", 1);
            
            int scheduleCount = 0;
            for (Timetable t : scheduleListItems) {
                if (scheduleCount >= 3) break;
                scheduleList.add(createScheduleItem("🕒", t.getSubjectName(), t.getStartTime() + " - " + t.getEndTime() + " • " + t.getClassroom()));
                if (scheduleCount < scheduleListItems.size() - 1 && scheduleCount < 2) {
                    scheduleList.add(Box.createRigidArea(new Dimension(0, 30)));
                }
                scheduleCount++;
            }
            if (scheduleListItems.isEmpty()) {
                JLabel emptyLabel = new JLabel("No schedule for today");
                emptyLabel.setFont(UIUtils.FONT_NORMAL);
                emptyLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
                scheduleList.add(emptyLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Failed to load schedule");
            errorLabel.setForeground(Color.RED);
            scheduleList.add(errorLabel);
        }
        
        timetableTop.add(scheduleList, BorderLayout.CENTER);
        
        timetableCard.add(timetableTop, BorderLayout.NORTH);
        
        // --- Academic Calendar ---
        DashboardCard calendarCard = new DashboardCard();
        calendarCard.setLayout(new BorderLayout());
        calendarCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel calendarTop = new JPanel(new BorderLayout());
        calendarTop.setOpaque(false);
        
        JLabel cTitle = new JLabel("Academic Calendar");
        cTitle.setFont(UIUtils.FONT_HEADER);
        calendarTop.add(cTitle, BorderLayout.NORTH);
        
        JPanel calendarList = new JPanel();
        calendarList.setLayout(new BoxLayout(calendarList, BoxLayout.Y_AXIS));
        calendarList.setOpaque(false);
        calendarList.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        try {
            // Student sees all events just like admin
            List<CalendarEvent> events = new EventService().getAllEvents();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            int count = 0;
            for (CalendarEvent ev : events) {
                if (count >= 3) break;
                String dateStr = ev.getEventDate() != null ? sdf.format(ev.getEventDate()) : "Unknown";
                // Reuse createScheduleItem for formatting, passing type as room
                calendarList.add(createScheduleItem("📅", ev.getTitle(), dateStr + " • " + ev.getEventType()));
                if (count < events.size() - 1 && count < 2) {
                    calendarList.add(Box.createRigidArea(new Dimension(0, 30)));
                }
                count++;
            }
            if (events.isEmpty()) {
                JLabel emptyLabel = new JLabel("No upcoming events");
                emptyLabel.setFont(UIUtils.FONT_NORMAL);
                emptyLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
                calendarList.add(emptyLabel);
            }
        } catch (Exception e) {}
        calendarTop.add(calendarList, BorderLayout.CENTER);
        calendarCard.add(calendarTop, BorderLayout.NORTH);
        
        DashboardCard noticeCard = new DashboardCard();
        noticeCard.setLayout(new BorderLayout());
        noticeCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel noticeTop = new JPanel(new BorderLayout());
        noticeTop.setOpaque(false);
        
        JLabel nTitle = new JLabel("Recent Notices");
        nTitle.setFont(UIUtils.FONT_HEADER);
        noticeTop.add(nTitle, BorderLayout.NORTH);
        
        JPanel noticeList = new JPanel();
        noticeList.setLayout(new BoxLayout(noticeList, BoxLayout.Y_AXIS));
        noticeList.setOpaque(false);
        noticeList.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        try {
            NoticeService noticeService = new NoticeService();
            List<Notice> notices = noticeService.getAllNotices();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            Color[] colors = {new Color(253, 126, 20), new Color(220, 53, 69), new Color(111, 66, 193), new Color(13, 110, 253), new Color(40, 167, 69)};
            
            int count = 0;
            for (Notice notice : notices) {
                if (count >= 3) break;
                String dateStr = notice.getPublishDate() != null ? sdf.format(notice.getPublishDate()) : "Unknown Date";
                Color iconColor = colors[count % colors.length];
                noticeList.add(createNoticeItem(notice, iconColor));
                
                if (count < notices.size() - 1 && count < 2) {
                    noticeList.add(Box.createRigidArea(new Dimension(0, 30)));
                }
                count++;
            }
            if (notices.isEmpty()) {
                JLabel emptyLabel = new JLabel("No recent notices");
                emptyLabel.setFont(UIUtils.FONT_NORMAL);
                emptyLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
                noticeList.add(emptyLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Failed to load notices");
            errorLabel.setForeground(Color.RED);
            noticeList.add(errorLabel);
        }
        
        noticeTop.add(noticeList, BorderLayout.CENTER);
        
        noticeCard.add(noticeTop, BorderLayout.NORTH);
        
        bottomContent.add(timetableCard);
        bottomContent.add(calendarCard);
        bottomContent.add(noticeCard);
        
        centerContent.add(bottomContent, BorderLayout.CENTER);
        
        add(centerContent, BorderLayout.CENTER);

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
    
    private JPanel createScheduleItem(String iconStr, String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(iconStr);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(UIUtils.FONT_SMALL);
        subLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(titleLabel);
        detailPanel.add(subLabel);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createNoticeItem(Notice notice, Color iconColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(StudentDashboardPanel.this, 
                    notice.getDescription(), 
                    notice.getTitle(), 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        };
        
        JLabel iconLabel = new JLabel("📄");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(iconColor); // Emoji might not tint, but good intent
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(notice.getTitle());
        titleLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String dateStr = notice.getPublishDate() != null ? sdf.format(notice.getPublishDate()) : "Unknown Date";
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(UIUtils.FONT_SMALL);
        dateLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(titleLabel);
        detailPanel.add(dateLabel);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);

        // Add click listener to all components
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(ma);
        detailPanel.addMouseListener(ma);
        titleLabel.addMouseListener(ma);
        dateLabel.addMouseListener(ma);
        iconLabel.addMouseListener(ma);

        return panel;
    }
}
