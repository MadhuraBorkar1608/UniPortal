package com.uniportal.ui.admin;

import com.uniportal.ui.common.DashboardCard;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import com.uniportal.service.NoticeService;
import com.uniportal.service.StudentService;
import com.uniportal.service.FacultyService;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.AssignmentService;
import com.uniportal.service.EventService;
import com.uniportal.service.HelpdeskService;
import com.uniportal.service.AttendanceService;
import com.uniportal.model.Notice;
import com.uniportal.model.AttendanceRecord;
import com.uniportal.model.HelpdeskTicket;
import com.uniportal.model.Assignment;
import com.uniportal.model.CalendarEvent;
import java.util.List;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        
        String totalStudents = "0";
        String totalFaculty = "0";
        String departments = "0";
        String totalNotices = "0";
        String assignments = "0";
        String events = "0";
        String openTickets = "0";
        String avgAttendance = "0%";
        
        try {
            totalStudents = String.valueOf(new StudentService().getAllStudents().size());
            totalFaculty = String.valueOf(new FacultyService().getAllFaculty().size());
            departments = String.valueOf(new DepartmentService().getAllDepartments().size());
            totalNotices = String.valueOf(new NoticeService().getAllNotices().size());
            assignments = String.valueOf(new AssignmentService().getAllAssignments().size());
            events = String.valueOf(new EventService().getAllEvents().size());
            
            long openCount = new HelpdeskService().getAllTickets().stream()
                .filter(t -> "OPEN".equalsIgnoreCase(t.getStatus())).count();
            openTickets = String.valueOf(openCount);
            
            List<AttendanceRecord> attendanceRecords = new AttendanceService().getAllRecords();
            if (!attendanceRecords.isEmpty()) {
                long presentCount = attendanceRecords.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
                avgAttendance = (presentCount * 100 / attendanceRecords.size()) + "%";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardsPanel.add(createSummaryCard("Total Students", totalStudents, new Color(13, 110, 253)));
        cardsPanel.add(createSummaryCard("Total Faculty", totalFaculty, new Color(40, 167, 69)));
        cardsPanel.add(createSummaryCard("Departments", departments, new Color(111, 66, 193)));
        cardsPanel.add(createSummaryCard("Total Notices", totalNotices, new Color(253, 126, 20)));
        
        cardsPanel.add(createSummaryCard("Assignments", assignments, new Color(23, 162, 184)));
        cardsPanel.add(createSummaryCard("Events", events, new Color(220, 53, 69)));
        cardsPanel.add(createSummaryCard("Open Tickets", openTickets, new Color(255, 193, 7)));
        cardsPanel.add(createSummaryCard("Avg Attendance", avgAttendance, new Color(32, 201, 151)));
        
        centerContent.add(cardsPanel, BorderLayout.NORTH);
        
        // Notice panel in the center
        JPanel bottomContent = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomContent.setOpaque(false);
        
        // --- System Activity ---
        DashboardCard activityCard = new DashboardCard();
        activityCard.setLayout(new BorderLayout());
        activityCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel activityTop = new JPanel(new BorderLayout());
        activityTop.setOpaque(false);
        
        JLabel aTitle = new JLabel("Recent System Activity");
        aTitle.setFont(UIUtils.FONT_HEADER);
        activityTop.add(aTitle, BorderLayout.NORTH);
        
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityList.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        try {
            List<Notice> actNotices = new NoticeService().getAllNotices();
            List<CalendarEvent> actEvents = new EventService().getAllEvents();
            List<Assignment> actAssignments = new AssignmentService().getAllAssignments();
            
            if (!actNotices.isEmpty()) {
                activityList.add(createActivityItem("📢", "New Notice: " + actNotices.get(0).getTitle(), "Recently"));
                activityList.add(Box.createRigidArea(new Dimension(0, 30)));
            }
            if (!actEvents.isEmpty()) {
                activityList.add(createActivityItem("📅", "New Event: " + actEvents.get(0).getTitle(), "Recently"));
                activityList.add(Box.createRigidArea(new Dimension(0, 30)));
            }
            if (!actAssignments.isEmpty()) {
                activityList.add(createActivityItem("📝", "New Assignment: " + actAssignments.get(0).getTitle(), "Recently"));
            }
        } catch (Exception e) {}
        
        activityTop.add(activityList, BorderLayout.CENTER);
        activityCard.add(activityTop, BorderLayout.NORTH);

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
            List<CalendarEvent> calEvents = new EventService().getAllEvents();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            int count = 0;
            for (CalendarEvent ev : calEvents) {
                if (count >= 3) break;
                String dateStr = ev.getEventDate() != null ? sdf.format(ev.getEventDate()) : "Unknown";
                calendarList.add(createScheduleItem("📅", ev.getTitle(), dateStr + " • " + ev.getEventType()));
                if (count < calEvents.size() - 1 && count < 2) {
                    calendarList.add(Box.createRigidArea(new Dimension(0, 30)));
                }
                count++;
            }
        } catch (Exception e) {}
        calendarTop.add(calendarList, BorderLayout.CENTER);
        calendarCard.add(calendarTop, BorderLayout.NORTH);

        // --- Recent Notices ---
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
        bottomContent.add(activityCard);
        bottomContent.add(calendarCard);
        bottomContent.add(noticeCard);
        
        centerContent.add(bottomContent, BorderLayout.CENTER);
        
        add(centerContent, BorderLayout.CENTER);

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
    
    private JPanel createActivityItem(String iconStr, String text, String time) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel dotLabel = new JLabel(iconStr);
        dotLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(UIUtils.FONT_SMALL);
        timeLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(textLabel);
        detailPanel.add(timeLabel);
        
        panel.add(dotLabel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createScheduleItem(String iconStr, String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(iconStr);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.setOpaque(false);
        JLabel subLabel = new JLabel(title);
        subLabel.setFont(UIUtils.FONT_NORMAL.deriveFont(Font.BOLD));
        JLabel roomLabel = new JLabel(subtitle);
        roomLabel.setFont(UIUtils.FONT_SMALL);
        roomLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        detailPanel.add(subLabel);
        detailPanel.add(roomLabel);
        
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
                JOptionPane.showMessageDialog(AdminDashboardPanel.this, 
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
