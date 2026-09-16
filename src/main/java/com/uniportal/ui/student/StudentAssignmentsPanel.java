package com.uniportal.ui.student;

import com.uniportal.model.Assignment;
import com.uniportal.model.AssignmentSubmission;
import com.uniportal.model.Student;
import com.uniportal.service.AssignmentService;
import com.uniportal.service.AssignmentSubmissionService;
import com.uniportal.service.StudentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentAssignmentsPanel extends JPanel {

    private AssignmentService assignmentService;
    private AssignmentSubmissionService submissionService;
    private StudentService studentService;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Assignment> assignments;
    private List<AssignmentSubmission> mySubmissions;

    public StudentAssignmentsPanel() {
        assignmentService = new AssignmentService();
        submissionService = new AssignmentSubmissionService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Assignments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Assignment ID", "Title", "Subject", "Deadline", "Status", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton submitBtn = new StyledButton("Submit Assignment");
        
        submitBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                Assignment assignment = null;
                for (Assignment a : assignments) {
                    if (a.getId() == id) { assignment = a; break; }
                }
                if (assignment != null) {
                    Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
                    String status = (String) tableModel.getValueAt(row, 4);
                    if ("PENDING".equals(status)) {
                        com.uniportal.ui.admin.assignments.StudentSubmitDialog dialog = new com.uniportal.ui.admin.assignments.StudentSubmitDialog(SwingUtilities.getWindowAncestor(this), assignment, s.getStudentId());
                        dialog.setVisible(true);
                        if (dialog.isSubmitted()) {
                            loadData();
                        }
                    } else {
                        UIUtils.showError(this, "You have already submitted this assignment.");
                    }
                }
            } else {
                UIUtils.showError(this, "Please select an assignment.");
            }
        });

        bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null && s.getCourseId() > 0) {
                assignments = assignmentService.getAssignmentsForStudent(s.getCourseId(), s.getCurrentSemester());
                mySubmissions = submissionService.getSubmissionsByStudent(s.getStudentId());
                
                for (Assignment a : assignments) {
                    String status = "PENDING";
                    String grade = "-";
                    for (AssignmentSubmission sub : mySubmissions) {
                        if (sub.getAssignmentId() == a.getId()) {
                            status = sub.getStatus();
                            grade = sub.getGrade() != null ? sub.getGrade() : "-";
                            break;
                        }
                    }
                    
                    tableModel.addRow(new Object[]{
                        a.getId(), a.getTitle(), a.getSubjectName(), a.getDeadline(), status, grade
                    });
                }
            }
        }
    }
}
