package com.uniportal.ui.admin.students;

import com.uniportal.model.Course;
import com.uniportal.model.Department;
import com.uniportal.model.Student;
import com.uniportal.service.CourseService;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.StudentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentFormDialog extends JDialog {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Department> deptCombo;
    private JComboBox<Course> courseCombo;
    private JTextField semField;
    private JTextField divField;
    private JTextArea addressArea;
    private JPasswordField passField;
    private boolean saved = false;
    private Student student;
    private StudentService service;

    public StudentFormDialog(Window parent, Student s, StudentService service) {
        super(parent, s == null ? "Add Student" : "Edit Student", ModalityType.APPLICATION_MODAL);
        this.student = s;
        this.service = service;

        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        if (s != null) idField.setEditable(false);
        formPanel.add(idField);
        
        if (s == null) {
            formPanel.add(new JLabel("Initial Password:"));
            passField = new JPasswordField();
            formPanel.add(passField);
        } else {
            formPanel.add(new JLabel("")); // Spacer
            formPanel.add(new JLabel(""));
        }

        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Department:"));
        deptCombo = new JComboBox<>();
        loadDepartments();
        formPanel.add(deptCombo);

        formPanel.add(new JLabel("Course:"));
        courseCombo = new JComboBox<>();
        loadCourses();
        formPanel.add(courseCombo);

        formPanel.add(new JLabel("Semester:"));
        semField = new JTextField();
        formPanel.add(semField);

        formPanel.add(new JLabel("Division:"));
        divField = new JTextField();
        formPanel.add(divField);

        formPanel.add(new JLabel("Address:"));
        addressArea = new JTextArea();
        addressArea.setLineWrap(true);
        formPanel.add(new JScrollPane(addressArea));

        if (s != null) {
            idField.setText(s.getStudentId());
            nameField.setText(s.getFullName());
            emailField.setText(s.getEmail());
            phoneField.setText(s.getPhone());
            semField.setText(String.valueOf(s.getCurrentSemester()));
            divField.setText(s.getDivision());
            addressArea.setText(s.getAddress());
            setDeptCombo(s.getDeptId());
            setCourseCombo(s.getCourseId());
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
    
    private void loadCourses() {
        CourseService cs = new CourseService();
        List<Course> courses = cs.getAllCourses();
        for (Course c : courses) {
            courseCombo.addItem(c);
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
    
    private void setCourseCombo(int courseId) {
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            if (courseCombo.getItemAt(i).getId() == courseId) {
                courseCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void save() {
        try {
            Department selDept = (Department) deptCombo.getSelectedItem();
            if (selDept == null) throw new Exception("Please select a department.");
            
            Course selCourse = (Course) courseCombo.getSelectedItem();
            if (selCourse == null) throw new Exception("Please select a course.");
            
            int sem = Integer.parseInt(semField.getText());
            
            if (student == null) {
                student = new Student();
                student.setStudentId(idField.getText());
                student.setFullName(nameField.getText());
                student.setEmail(emailField.getText());
                student.setPhone(phoneField.getText());
                student.setDeptId(selDept.getId());
                student.setCourseId(selCourse.getId());
                student.setCurrentSemester(sem);
                student.setDivision(divField.getText());
                student.setAddress(addressArea.getText());
                
                String initPass = new String(passField.getPassword());
                service.addStudent(student, initPass);
            } else {
                student.setFullName(nameField.getText());
                student.setEmail(emailField.getText());
                student.setPhone(phoneField.getText());
                student.setDeptId(selDept.getId());
                student.setCourseId(selCourse.getId());
                student.setCurrentSemester(sem);
                student.setDivision(divField.getText());
                student.setAddress(addressArea.getText());
                service.updateStudent(student);
            }
            saved = true;
            UIUtils.showSuccess(this, "Saved successfully.");
            dispose();
        } catch (NumberFormatException ex) {
            UIUtils.showError(this, "Semester must be a number.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
