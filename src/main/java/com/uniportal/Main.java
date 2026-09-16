package com.uniportal;

import com.formdev.flatlaf.FlatLightLaf;
import com.uniportal.ui.common.MainFrame;
import com.uniportal.util.UIUtils;
import javax.swing.SwingUtilities;

public class Main {
    
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                UIUtils.setupCustomUI();
                UIUtils.setGlobalFont(UIUtils.FONT_NORMAL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mainFrame = new MainFrame();
            
            com.uniportal.ui.auth.LoginFrame loginFrame = new com.uniportal.ui.auth.LoginFrame();
            mainFrame.addPanel("Login", loginFrame);
            
            mainFrame.addPanel("StudentDashboard", new com.uniportal.ui.student.StudentDashboardPanel());
            mainFrame.addPanel("AdminDashboard", new com.uniportal.ui.admin.AdminDashboardPanel());
            mainFrame.addPanel("StudentProfile", new com.uniportal.ui.student.StudentProfilePanel());
            mainFrame.addPanel("AdminProfile", new com.uniportal.ui.admin.AdminProfilePanel());
            
            // Phase 4: Master Data
            mainFrame.addPanel("ManageDepartments", new com.uniportal.ui.admin.departments.DepartmentManagementPanel());
            mainFrame.addPanel("ManageCourses", new com.uniportal.ui.admin.courses.CourseManagementPanel());
            mainFrame.addPanel("ManageSubjects", new com.uniportal.ui.admin.subjects.SubjectManagementPanel());
            mainFrame.addPanel("ManageFaculty", new com.uniportal.ui.admin.faculty.FacultyManagementPanel());
            mainFrame.addPanel("ManageStudents", new com.uniportal.ui.admin.students.StudentManagementPanel());
            mainFrame.addPanel("ManageNotices", new com.uniportal.ui.admin.notices.NoticeManagementPanel());
            mainFrame.addPanel("ManageTimetable", new com.uniportal.ui.admin.timetable.TimetableManagementPanel());
            mainFrame.addPanel("ManageStudyMaterial", new com.uniportal.ui.admin.studymaterial.StudyMaterialManagementPanel());
            mainFrame.addPanel("ManageAttendance", new com.uniportal.ui.admin.attendance.AttendanceManagementPanel());
            mainFrame.addPanel("ManageAssignments", new com.uniportal.ui.admin.assignments.AssignmentManagementPanel());
            mainFrame.addPanel("ManageCalendar", new com.uniportal.ui.admin.calendar.CalendarManagementPanel());
            mainFrame.addPanel("ManageEvents", new com.uniportal.ui.admin.events.EventManagementPanel());
            mainFrame.addPanel("ManageHelpdesk", new com.uniportal.ui.admin.helpdesk.HelpdeskManagementPanel());
            
            // Phase 5: Student Services
            mainFrame.addPanel("StudentTimetable", new com.uniportal.ui.student.StudentTimetablePanel());
            mainFrame.addPanel("StudentMaterials", new com.uniportal.ui.student.StudentStudyMaterialPanel());
            
            // Phase 6: Events & Helpdesk
            mainFrame.addPanel("StudentEvents", new com.uniportal.ui.student.StudentEventPanel());
            mainFrame.addPanel("StudentHelpdesk", new com.uniportal.ui.student.StudentHelpdeskPanel());
            
            // Phase 7: More Student Services
            mainFrame.addPanel("StudentAssignments", new com.uniportal.ui.student.StudentAssignmentsPanel());
            mainFrame.addPanel("StudentAttendance", new com.uniportal.ui.student.StudentAttendancePanel());
            mainFrame.addPanel("StudentNotices", new com.uniportal.ui.student.StudentNoticesPanel());
            mainFrame.addPanel("StudentCalendar", new com.uniportal.ui.student.StudentCalendarPanel());
            
            mainFrame.showPanel("Login", "Login");
            mainFrame.setVisible(true);
        });
    }
    
    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
