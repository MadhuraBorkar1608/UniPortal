package com.uniportal.ui.admin.courses;

import com.uniportal.model.Course;
import com.uniportal.model.Department;
import com.uniportal.service.CourseService;
import com.uniportal.service.DepartmentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseManagementPanel extends JPanel {

    private CourseService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public CourseManagementPanel() {
        service = new CourseService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        StyledButton addBtn = new StyledButton("Add Course");
        addBtn.addActionListener(e -> showFormDialog(null));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Code", "Name", "Department", "Duration (Yrs)", "DeptID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        // Hide DeptID column
        table.removeColumn(table.getColumnModel().getColumn(5));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton editBtn = new StyledButton("Edit");
        StyledButton deleteBtn = new StyledButton("Delete");

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Course c = getCourseFromRow(row);
                showFormDialog(c);
            } else {
                UIUtils.showError(this, "Please select a course to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if (UIUtils.confirm(this, "Are you sure you want to delete this course?")) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    try {
                        service.deleteCourse(id);
                        UIUtils.showSuccess(this, "Deleted successfully.");
                        loadData();
                    } catch (Exception ex) {
                        UIUtils.showError(this, ex.getMessage());
                    }
                }
            } else {
                UIUtils.showError(this, "Please select a course to delete.");
            }
        });

        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Course> list = service.getAllCourses();
        for (Course c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getCourseCode(), c.getCourseName(), c.getDeptName(), c.getDurationYears(), c.getDeptId()});
        }
    }

    private Course getCourseFromRow(int row) {
        Course c = new Course();
        c.setId((int) tableModel.getValueAt(row, 0));
        c.setCourseCode((String) tableModel.getValueAt(row, 1));
        c.setCourseName((String) tableModel.getValueAt(row, 2));
        c.setDeptName((String) tableModel.getValueAt(row, 3));
        c.setDurationYears((int) tableModel.getValueAt(row, 4));
        c.setDeptId((int) tableModel.getValueAt(row, 5));
        return c;
    }

    private void showFormDialog(Course c) {
        CourseFormDialog dialog = new CourseFormDialog(SwingUtilities.getWindowAncestor(this), c, service);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
}
