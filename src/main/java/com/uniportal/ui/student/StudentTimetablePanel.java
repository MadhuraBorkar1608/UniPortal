package com.uniportal.ui.student;

import com.uniportal.model.Student;
import com.uniportal.model.Timetable;
import com.uniportal.service.StudentService;
import com.uniportal.service.TimetableService;
import com.uniportal.ui.common.TimetableMatrixPanel;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentTimetablePanel extends JPanel {

    private TimetableService timetableService;
    private StudentService studentService;
    private JPanel gridContainer;

    public StudentTimetablePanel() {
        timetableService = new TimetableService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Class Timetable");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        gridContainer = new JPanel(new BorderLayout());
        gridContainer.setOpaque(false);
        add(gridContainer, BorderLayout.CENTER);
    }
    
    public void loadData() {
        gridContainer.removeAll();
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                List<Timetable> schedule = timetableService.getStudentTimetable(s.getDivision(), s.getDeptId());
                TimetableMatrixPanel matrixPanel = new TimetableMatrixPanel(schedule);
                gridContainer.add(matrixPanel, BorderLayout.CENTER);
            }
        }
        gridContainer.revalidate();
        gridContainer.repaint();
    }
}
