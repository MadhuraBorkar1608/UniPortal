package com.uniportal.ui.common;

import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    
    private SidebarPanel sidebar;
    private HeaderPanel header;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("UniPortal");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new SidebarPanel();
        add(sidebar, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        header = new HeaderPanel();
        rightPanel.add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);
    }
    
    public void addPanel(String name, JPanel panel) {
        contentPanel.add(panel, name);
    }
    
    public void showPanel(String name, String title) {
        header.setTitle(title);
        header.setIconsVisible(!"Login".equals(name));
        sidebar.setVisible(!"Login".equals(name));
        
        try {
            for (Component comp : contentPanel.getComponents()) {
                if ("StudentProfile".equals(name) && comp instanceof com.uniportal.ui.student.StudentProfilePanel) {
                    ((com.uniportal.ui.student.StudentProfilePanel) comp).loadProfile();
                } else if ("AdminProfile".equals(name) && comp instanceof com.uniportal.ui.admin.AdminProfilePanel) {
                    ((com.uniportal.ui.admin.AdminProfilePanel) comp).loadProfile();
                }
            }
        } catch (Exception e) {}
        
        cardLayout.show(contentPanel, name);
    }
    
    public SidebarPanel getSidebar() {
        return sidebar;
    }
    
    public void refreshUserInfo() {
        if (SessionManager.isAuthenticated()) {
            String role = SessionManager.getCurrentUser().getRole();
            String name = SessionManager.getCurrentUser().getUsername();
            header.setUserInfo(name, role);
            setupSidebar(role);
        } else {
            header.setUserInfo("", "");
            sidebar.setMenuStateForRole("");
        }
    }
    
    private void setupSidebar(String role) {
        sidebar.setMenuStateForRole(role);
        
        if ("STUDENT".equals(role)) {
            sidebar.addMenuButton("Dashboard", "StudentDashboard", e -> showPanel("StudentDashboard", "Student Dashboard"));
            
            sidebar.addMenuButton("My Timetable", "StudentTimetable", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentTimetablePanel) {
                        try { try { ((com.uniportal.ui.student.StudentTimetablePanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentTimetable", "Class Timetable");
            });
            
            sidebar.addMenuButton("Study Material", "StudentMaterials", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentStudyMaterialPanel) {
                        try { try { ((com.uniportal.ui.student.StudentStudyMaterialPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentMaterials", "Study Material");
            });
            
            sidebar.addMenuButton("Academic Calendar", "StudentEvents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentEventPanel) {
                        try { try { ((com.uniportal.ui.student.StudentEventPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
                showPanel("StudentEvents", "Academic Calendar");
            });
            
            sidebar.addMenuButton("Helpdesk", "StudentHelpdesk", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentHelpdeskPanel) {
                        try { try { ((com.uniportal.ui.student.StudentHelpdeskPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentHelpdesk", "Helpdesk");
            });
            
            sidebar.addMenuButton("Assignments", "StudentAssignments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentAssignmentsPanel) {
                        try { try { ((com.uniportal.ui.student.StudentAssignmentsPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentAssignments", "Assignments");
            });

            sidebar.addMenuButton("Attendance", "StudentAttendance", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentAttendancePanel) {
                        try { try { ((com.uniportal.ui.student.StudentAttendancePanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentAttendance", "Attendance");
            });

            sidebar.addMenuButton("Notices", "StudentNotices", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentNoticesPanel) {
                        try { try { ((com.uniportal.ui.student.StudentNoticesPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentNotices", "Notices");
            });

            sidebar.addMenuButton("Calendar", "StudentCalendar", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentCalendarPanel) {
                        try { try { ((com.uniportal.ui.student.StudentCalendarPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("StudentCalendar", "Academic Calendar");
            });

            // Add stubs for future menus
            sidebar.addMenuButton("Change Password", "ChangePassword", e -> new com.uniportal.ui.auth.ChangePasswordDialog(this).setVisible(true));
        } else if ("ADMIN".equals(role)) {
            sidebar.addMenuButton("Dashboard", "AdminDashboard", e -> showPanel("AdminDashboard", "Admin Dashboard"));
            
            // Phase 4: Master Data
            sidebar.addMenuButton("Departments", "ManageDepartments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.departments.DepartmentManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.departments.DepartmentManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageDepartments", "Manage Departments");
            });
            sidebar.addMenuButton("Courses", "ManageCourses", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.courses.CourseManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.courses.CourseManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageCourses", "Manage Courses");
            });
            sidebar.addMenuButton("Subjects", "ManageSubjects", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.subjects.SubjectManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.subjects.SubjectManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageSubjects", "Manage Subjects");
            });
            sidebar.addMenuButton("Faculty", "ManageFaculty", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.faculty.FacultyManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.faculty.FacultyManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageFaculty", "Manage Faculty");
            });
            sidebar.addMenuButton("Students", "ManageStudents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.students.StudentManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.students.StudentManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageStudents", "Manage Students");
            });
            sidebar.addMenuButton("Notices", "ManageNotices", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.notices.NoticeManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.notices.NoticeManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageNotices", "Manage Notices");
            });
            sidebar.addMenuButton("Timetable", "ManageTimetable", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.timetable.TimetableManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.timetable.TimetableManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageTimetable", "Manage Timetable");
            });
            sidebar.addMenuButton("Study Material", "ManageStudyMaterial", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.studymaterial.StudyMaterialManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.studymaterial.StudyMaterialManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageStudyMaterial", "Manage Study Material");
            });
            sidebar.addMenuButton("Attendance", "ManageAttendance", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.attendance.AttendanceManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.attendance.AttendanceManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageAttendance", "Manage Attendance");
            });
            sidebar.addMenuButton("Assignments", "ManageAssignments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.assignments.AssignmentManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.assignments.AssignmentManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageAssignments", "Manage Assignments");
            });
            sidebar.addMenuButton("Calendar", "ManageCalendar", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.calendar.CalendarManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.calendar.CalendarManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageCalendar", "Manage Calendar");
            });
            sidebar.addMenuButton("Academic Calendar", "ManageEvents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.events.EventManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.events.EventManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
                showPanel("ManageEvents", "Academic Calendar");
            });
            sidebar.addMenuButton("Helpdesk", "ManageHelpdesk", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.helpdesk.HelpdeskManagementPanel) {
                        try { try { ((com.uniportal.ui.admin.helpdesk.HelpdeskManagementPanel) comp).loadData(); } catch (Exception ex) {} } catch (Exception ex) { ex.printStackTrace(); }
                        break;
                    }
                }
                showPanel("ManageHelpdesk", "Manage Helpdesk");
            });
            
            // Add stubs for future menus
            sidebar.addMenuButton("Change Password", "ChangePassword", e -> new com.uniportal.ui.auth.ChangePasswordDialog(this).setVisible(true));
        }
        
        sidebar.addMenuButton("Logout", "Logout", e -> logout());
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private void logout() {
        SessionManager.logout();
        showPanel("Login", "Login");
        refreshUserInfo();
    }
}
