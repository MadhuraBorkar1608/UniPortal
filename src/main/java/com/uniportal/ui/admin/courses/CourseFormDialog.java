package com.uniportal.ui.admin.courses;

import com.uniportal.model.Course;
import com.uniportal.model.Department;
import com.uniportal.service.CourseService;
import com.uniportal.service.DepartmentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseFormDialog extends JDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JComboBox<Department> deptCombo;
    private JTextField durationField;
    private boolean saved = false;
    private Course course;
    private CourseService service;

    public CourseFormDialog(Window parent, Course c, CourseService service) {
        super(parent, c == null ? "Add Course" : "Edit Course", ModalityType.APPLICATION_MODAL);
        this.course = c;
        this.service = service;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Course Code:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Course Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Department:"));
        deptCombo = new JComboBox<>();
        loadDepartments();
        formPanel.add(deptCombo);

        formPanel.add(new JLabel("Duration (Years):"));
        durationField = new JTextField();
        formPanel.add(durationField);

        if (c != null) {
            codeField.setText(c.getCourseCode());
            nameField.setText(c.getCourseName());
            durationField.setText(String.valueOf(c.getDurationYears()));
            setDeptCombo(c.getDeptId());
        }

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        saveBtn.addActionListener(e -> save());
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadDepartments() {
        DepartmentService ds = new DepartmentService();
        List<Department> depts = ds.getAllDepartments();
        for (Department d : depts) {
            deptCombo.addItem(d);
        }
    }
    
    private void setDeptCombo(int deptId) {
        for (int i = 0; i < deptCombo.getItemCount(); i++) {
            if (deptCombo.getItemAt(i).getId() == deptId) {
                deptCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void save() {
        try {
            Department selDept = (Department) deptCombo.getSelectedItem();
            if (selDept == null) throw new Exception("Please select a department.");
            int dur = Integer.parseInt(durationField.getText());
            
            if (course == null) {
                course = new Course();
                course.setCourseCode(codeField.getText());
                course.setCourseName(nameField.getText());
                course.setDeptId(selDept.getId());
                course.setDurationYears(dur);
                service.addCourse(course);
            } else {
                course.setCourseCode(codeField.getText());
                course.setCourseName(nameField.getText());
                course.setDeptId(selDept.getId());
                course.setDurationYears(dur);
                service.updateCourse(course);
            }
            saved = true;
            UIUtils.showSuccess(this, "Saved successfully.");
            dispose();
        } catch (NumberFormatException ex) {
            UIUtils.showError(this, "Duration must be a number.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
