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
            sidebar.addMenuButton("My Profile", "StudentProfile", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentProfilePanel) {
                        ((com.uniportal.ui.student.StudentProfilePanel) comp).loadProfile();
                        break;
                    }
                }
                showPanel("StudentProfile", "My Profile");
            });
            
            sidebar.addMenuButton("My Timetable", "StudentTimetable", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentTimetablePanel) {
                        ((com.uniportal.ui.student.StudentTimetablePanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentTimetable", "Class Timetable");
            });
            
            sidebar.addMenuButton("Study Material", "StudentMaterials", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentStudyMaterialPanel) {
                        ((com.uniportal.ui.student.StudentStudyMaterialPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentMaterials", "Study Material");
            });
            
            sidebar.addMenuButton("Events", "StudentEvents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentEventPanel) {
                        ((com.uniportal.ui.student.StudentEventPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentEvents", "Events & Activities");
            });
            
            sidebar.addMenuButton("Helpdesk", "StudentHelpdesk", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentHelpdeskPanel) {
                        ((com.uniportal.ui.student.StudentHelpdeskPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentHelpdesk", "Helpdesk");
            });
            
            sidebar.addMenuButton("Assignments", "StudentAssignments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentAssignmentsPanel) {
                        ((com.uniportal.ui.student.StudentAssignmentsPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentAssignments", "Assignments");
            });

            sidebar.addMenuButton("Attendance", "StudentAttendance", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentAttendancePanel) {
                        ((com.uniportal.ui.student.StudentAttendancePanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentAttendance", "Attendance");
            });

            sidebar.addMenuButton("Notices", "StudentNotices", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentNoticesPanel) {
                        ((com.uniportal.ui.student.StudentNoticesPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentNotices", "Notices");
            });

            sidebar.addMenuButton("Calendar", "StudentCalendar", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.student.StudentCalendarPanel) {
                        ((com.uniportal.ui.student.StudentCalendarPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("StudentCalendar", "Academic Calendar");
            });

            // Add stubs for future menus
            sidebar.addMenuButton("Change Password", "ChangePassword", e -> new com.uniportal.ui.auth.ChangePasswordDialog(this).setVisible(true));
        } else if ("ADMIN".equals(role)) {
            sidebar.addMenuButton("Dashboard", "AdminDashboard", e -> showPanel("AdminDashboard", "Admin Dashboard"));
            sidebar.addMenuButton("My Profile", "AdminProfile", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.AdminProfilePanel) {
                        ((com.uniportal.ui.admin.AdminProfilePanel) comp).loadProfile();
                        break;
                    }
                }
                showPanel("AdminProfile", "My Profile");
            });
            
            // Phase 4: Master Data
            sidebar.addMenuButton("Departments", "ManageDepartments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.departments.DepartmentManagementPanel) {
                        ((com.uniportal.ui.admin.departments.DepartmentManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageDepartments", "Manage Departments");
            });
            sidebar.addMenuButton("Courses", "ManageCourses", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.courses.CourseManagementPanel) {
                        ((com.uniportal.ui.admin.courses.CourseManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageCourses", "Manage Courses");
            });
            sidebar.addMenuButton("Subjects", "ManageSubjects", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.subjects.SubjectManagementPanel) {
                        ((com.uniportal.ui.admin.subjects.SubjectManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageSubjects", "Manage Subjects");
            });
            sidebar.addMenuButton("Faculty", "ManageFaculty", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.faculty.FacultyManagementPanel) {
                        ((com.uniportal.ui.admin.faculty.FacultyManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageFaculty", "Manage Faculty");
            });
            sidebar.addMenuButton("Students", "ManageStudents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.students.StudentManagementPanel) {
                        ((com.uniportal.ui.admin.students.StudentManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageStudents", "Manage Students");
            });
            sidebar.addMenuButton("Notices", "ManageNotices", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.notices.NoticeManagementPanel) {
                        ((com.uniportal.ui.admin.notices.NoticeManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageNotices", "Manage Notices");
            });
            sidebar.addMenuButton("Timetable", "ManageTimetable", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.timetable.TimetableManagementPanel) {
                        ((com.uniportal.ui.admin.timetable.TimetableManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageTimetable", "Manage Timetable");
            });
            sidebar.addMenuButton("Study Material", "ManageStudyMaterial", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.studymaterial.StudyMaterialManagementPanel) {
                        ((com.uniportal.ui.admin.studymaterial.StudyMaterialManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageStudyMaterial", "Manage Study Material");
            });
            sidebar.addMenuButton("Attendance", "ManageAttendance", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.attendance.AttendanceManagementPanel) {
                        ((com.uniportal.ui.admin.attendance.AttendanceManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageAttendance", "Manage Attendance");
            });
            sidebar.addMenuButton("Assignments", "ManageAssignments", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.assignments.AssignmentManagementPanel) {
                        ((com.uniportal.ui.admin.assignments.AssignmentManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageAssignments", "Manage Assignments");
            });
            sidebar.addMenuButton("Calendar", "ManageCalendar", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.calendar.CalendarManagementPanel) {
                        ((com.uniportal.ui.admin.calendar.CalendarManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageCalendar", "Manage Calendar");
            });
            sidebar.addMenuButton("Events", "ManageEvents", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.events.EventManagementPanel) {
                        ((com.uniportal.ui.admin.events.EventManagementPanel) comp).loadData();
                        break;
                    }
                }
                showPanel("ManageEvents", "Manage Events");
            });
            sidebar.addMenuButton("Helpdesk", "ManageHelpdesk", e -> {
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof com.uniportal.ui.admin.helpdesk.HelpdeskManagementPanel) {
                        ((com.uniportal.ui.admin.helpdesk.HelpdeskManagementPanel) comp).loadData();
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
