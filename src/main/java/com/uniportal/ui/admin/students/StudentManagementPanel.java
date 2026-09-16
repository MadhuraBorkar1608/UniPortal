package com.uniportal.ui.admin.students;

import com.uniportal.model.Student;
import com.uniportal.service.StudentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementPanel extends JPanel {

    private StudentService service;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Student> studentsList;

    public StudentManagementPanel() {
        service = new StudentService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Student");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Name", "Email", "Phone", "Department", "Course", "Semester", "Division"};
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
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Student s = studentsList.get(row);
                showFormDialog(s);
            } else {
                UIUtils.showError(this, "Please select a student to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this student? This will also delete their login account.")) {
                    Student s = studentsList.get(row);
                    try {
                        service.deleteStudent(s);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a student to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        studentsList = service.getAllStudents();
        for (Student s : studentsList) {
            tableModel.addRow(new Object[]{
                s.getStudentId(), s.getFullName(), s.getEmail(), s.getPhone(),
                s.getDeptName(), s.getCourseName(), s.getCurrentSemester(), s.getDivision()
            });
        }
    }

    private void showFormDialog(Student s) {
        StudentFormDialog dialog = new StudentFormDialog(SwingUtilities.getWindowAncestor(this), s, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
