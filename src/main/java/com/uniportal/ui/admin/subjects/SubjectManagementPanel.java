package com.uniportal.ui.admin.subjects;

import com.uniportal.model.Subject;
import com.uniportal.service.SubjectService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubjectManagementPanel extends JPanel {

    private SubjectService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public SubjectManagementPanel() {
        service = new SubjectService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Subject");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Code", "Name", "Course", "Semester", "Credits", "Faculty", "CourseID", "FacultyID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        // Hide CourseID and FacultyID
        table.removeColumn(table.getColumnModel().getColumn(8)); // FacultyID
        table.removeColumn(table.getColumnModel().getColumn(7)); // CourseID

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Subject s = getSubjectFromRow(row);
                showFormDialog(s);
            } else {
                UIUtils.showError(this, "Please select a subject to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this subject?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteSubject(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a subject to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Subject> list = service.getAllSubjects();
        for (Subject s : list) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getSubjectCode(), s.getSubjectName(), 
                s.getCourseName(), s.getSemester(), s.getCredits(), 
                s.getFacultyName(), s.getCourseId(), s.getFacultyId()
            });
        }
    }

    private Subject getSubjectFromRow(int row) {
        Subject s = new Subject();
        s.setId((int) tableModel.getValueAt(row, 0));
        s.setSubjectCode((String) tableModel.getValueAt(row, 1));
        s.setSubjectName((String) tableModel.getValueAt(row, 2));
        s.setCourseName((String) tableModel.getValueAt(row, 3));
        s.setSemester((int) tableModel.getValueAt(row, 4));
        s.setCredits((int) tableModel.getValueAt(row, 5));
        s.setFacultyName((String) tableModel.getValueAt(row, 6));
        
        // These correspond to the hidden columns in the original model, 
        // since we removed them from the view, we must access the model directly
        s.setCourseId((int) tableModel.getValueAt(row, 7));
        s.setFacultyId((String) tableModel.getValueAt(row, 8));
        return s;
    }

    private void showFormDialog(Subject s) {
        SubjectFormDialog dialog = new SubjectFormDialog(SwingUtilities.getWindowAncestor(this), s, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
