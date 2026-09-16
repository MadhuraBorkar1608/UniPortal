package com.uniportal.ui.admin.assignments;

import com.uniportal.model.Assignment;
import com.uniportal.model.AssignmentSubmission;
import com.uniportal.service.AssignmentSubmissionService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignmentSubmissionsDialog extends JDialog {

    private Assignment assignment;
    private AssignmentSubmissionService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public AssignmentSubmissionsDialog(Window owner, Assignment assignment) {
        super(owner, "Submissions for: " + assignment.getTitle(), ModalityType.APPLICATION_MODAL);
        this.assignment = assignment;
        this.service = new AssignmentSubmissionService();
        
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Student", "Submission Time", "Status", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StyledButton gradeBtn = new StyledButton("Grade Submission");
        StyledButton closeBtn = new StyledButton("Close");
        
        gradeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                AssignmentSubmission sub = getSubmissionFromRow(row);
                GradeSubmissionDialog dialog = new GradeSubmissionDialog(this, sub, service);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadData();
                }
            } else {
                UIUtils.showError(this, "Please select a submission to grade.");
            }
        });
        
        closeBtn.addActionListener(e -> dispose());
        
        bottomPanel.add(gradeBtn);
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadData();
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<AssignmentSubmission> list = service.getSubmissionsForAssignment(assignment.getId());
        for (AssignmentSubmission sub : list) {
            tableModel.addRow(new Object[]{
                sub.getId(), sub.getStudentName() + " (" + sub.getStudentId() + ")", 
                sub.getSubmissionTime(), sub.getStatus(), sub.getGrade()
            });
        }
    }
    
    private AssignmentSubmission getSubmissionFromRow(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<AssignmentSubmission> list = service.getSubmissionsForAssignment(assignment.getId());
        for (AssignmentSubmission sub : list) {
            if (sub.getId() == id) return sub;
        }
        return null;
    }
}
