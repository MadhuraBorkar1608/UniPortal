package com.uniportal.ui.admin.notices;

import com.uniportal.model.Department;
import com.uniportal.model.Notice;
import com.uniportal.service.DepartmentService;
import com.uniportal.service.NoticeService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class NoticeFormDialog extends JDialog {

    private JTextField titleField;
    private JTextArea descArea;
    private JTextField categoryField;
    private JComboBox<DepartmentItem> deptCombo;
    private JTextField dateField;
    private JComboBox<String> statusCombo;
    
    private Notice notice;
    private NoticeService service;
    private boolean saved = false;

    public NoticeFormDialog(Window owner, Notice notice, NoticeService service) {
        super(owner, notice == null ? "Add Notice" : "Edit Notice", ModalityType.APPLICATION_MODAL);
        this.notice = notice;
        this.service = service;
        
        setSize(400, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(titleField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        gbc.gridx = 1; formPanel.add(new JScrollPane(descArea), gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Category:"), gbc);
        categoryField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(categoryField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Department:"), gbc);
        deptCombo = new JComboBox<>();
        deptCombo.addItem(new DepartmentItem(null, "All Departments"));
        loadDepartments();
        gbc.gridx = 1; formPanel.add(deptCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Publish Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        dateField.setText(new Date(System.currentTimeMillis()).toString());
        gbc.gridx = 1; formPanel.add(dateField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"DRAFT", "PUBLISHED"});
        gbc.gridx = 1; formPanel.add(statusCombo, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (notice != null) {
            titleField.setText(notice.getTitle());
            descArea.setText(notice.getDescription());
            categoryField.setText(notice.getCategory());
            dateField.setText(notice.getPublishDate() != null ? notice.getPublishDate().toString() : "");
            statusCombo.setSelectedItem(notice.getStatus());
            
            if (notice.getDeptId() != null) {
                for (int i = 0; i < deptCombo.getItemCount(); i++) {
                    DepartmentItem item = deptCombo.getItemAt(i);
                    if (item.id != null && item.id.equals(notice.getDeptId())) {
                        deptCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
    
    private void loadDepartments() {
        DepartmentService ds = new DepartmentService();
        List<Department> depts = ds.getAllDepartments();
        for (Department d : depts) {
            deptCombo.addItem(new DepartmentItem(d.getId(), d.getDeptName()));
        }
    }
    
    private void save() {
        try {
            Notice n = notice == null ? new Notice() : notice;
            n.setTitle(titleField.getText().trim());
            n.setDescription(descArea.getText().trim());
            n.setCategory(categoryField.getText().trim());
            DepartmentItem selectedDept = (DepartmentItem) deptCombo.getSelectedItem();
            n.setDeptId(selectedDept.id);
            n.setPublishDate(Date.valueOf(dateField.getText().trim()));
            n.setStatus((String) statusCombo.getSelectedItem());
            
            if (notice == null) {
                service.addNotice(n);
            } else {
                service.updateNotice(n);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            UIUtils.showError(this, "Invalid date format. Use YYYY-MM-DD.");
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    private static class DepartmentItem {
        Integer id;
        String name;
        DepartmentItem(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }
    }
}
