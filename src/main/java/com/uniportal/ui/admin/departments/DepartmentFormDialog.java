package com.uniportal.ui.admin.departments;

import com.uniportal.model.Department;
import com.uniportal.service.DepartmentService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;

public class DepartmentFormDialog extends JDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JTextArea descArea;
    private boolean saved = false;
    private Department department;
    private DepartmentService service;

    public DepartmentFormDialog(Window parent, Department d, DepartmentService service) {
        super(parent, d == null ? "Add Department" : "Edit Department", ModalityType.APPLICATION_MODAL);
        this.department = d;
        this.service = service;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Department Code:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Department Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description:"));
        descArea = new JTextArea();
        descArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(descArea);
        formPanel.add(scroll);

        if (d != null) {
            codeField.setText(d.getDeptCode());
            nameField.setText(d.getDeptName());
            descArea.setText(d.getDescription());
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

    private void save() {
        try {
            if (department == null) {
                department = new Department();
                department.setDeptCode(codeField.getText());
                department.setDeptName(nameField.getText());
                department.setDescription(descArea.getText());
                service.addDepartment(department);
            } else {
                department.setDeptCode(codeField.getText());
                department.setDeptName(nameField.getText());
                department.setDescription(descArea.getText());
                service.updateDepartment(department);
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
