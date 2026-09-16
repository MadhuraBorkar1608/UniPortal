package com.uniportal.ui.admin.faculty;

import com.uniportal.model.Department;
import com.uniportal.model.Faculty;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.FacultyService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FacultyFormDialog extends JDialog {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Department> deptCombo;
    private JTextField desigField;
    private boolean saved = false;
    private Faculty faculty;
    private FacultyService service;

    public FacultyFormDialog(Window parent, Faculty f, FacultyService service) {
        super(parent, f == null ? "Add Faculty" : "Edit Faculty", ModalityType.APPLICATION_MODAL);
        this.faculty = f;
        this.service = service;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Faculty ID:"));
        idField = new JTextField();
        if (f != null) idField.setEditable(false);
        formPanel.add(idField);

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

        formPanel.add(new JLabel("Designation:"));
        desigField = new JTextField();
        formPanel.add(desigField);

        if (f != null) {
            idField.setText(f.getFacultyId());
            nameField.setText(f.getFullName());
            emailField.setText(f.getEmail());
            phoneField.setText(f.getPhone());
            desigField.setText(f.getDesignation());
            setDeptCombo(f.getDeptId());
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
            
            if (faculty == null) {
                faculty = new Faculty();
                faculty.setFacultyId(idField.getText());
                faculty.setFullName(nameField.getText());
                faculty.setEmail(emailField.getText());
                faculty.setPhone(phoneField.getText());
                faculty.setDeptId(selDept.getId());
                faculty.setDesignation(desigField.getText());
                service.addFaculty(faculty);
            } else {
                faculty.setFullName(nameField.getText());
                faculty.setEmail(emailField.getText());
                faculty.setPhone(phoneField.getText());
                faculty.setDeptId(selDept.getId());
                faculty.setDesignation(desigField.getText());
                service.updateFaculty(faculty);
            }
            saved = true;
            UIUtils.showSuccess(this, "Saved successfully.");
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
