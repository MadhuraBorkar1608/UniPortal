package com.uniportal.ui.admin.studymaterial;

import com.uniportal.model.StudyMaterial;
import com.uniportal.model.Subject;
import com.uniportal.service.StudyMaterialService;
import com.uniportal.service.SubjectService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudyMaterialFormDialog extends JDialog {

    private JTextField titleField;
    private JComboBox<SubjectItem> subjectCombo;
    private JTextArea descArea;
    private JTextField fileNameField;
    private JTextField filePathField;
    
    private StudyMaterial material;
    private StudyMaterialService service;
    private boolean saved = false;

    public StudyMaterialFormDialog(Window owner, StudyMaterial material, StudyMaterialService service) {
        super(owner, material == null ? "Add Study Material" : "Edit Study Material", ModalityType.APPLICATION_MODAL);
        this.material = material;
        this.service = service;
        
        setSize(450, 450);
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
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Subject:"), gbc);
        subjectCombo = new JComboBox<>();
        loadSubjects();
        gbc.gridx = 1; formPanel.add(subjectCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        gbc.gridx = 1; formPanel.add(new JScrollPane(descArea), gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("File Name:"), gbc);
        fileNameField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(fileNameField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("File Path/URL:"), gbc);
        filePathField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(filePathField, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        StyledButton saveBtn = new StyledButton("Save");
        StyledButton cancelBtn = new StyledButton("Cancel");
        
        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        if (material != null) {
            titleField.setText(material.getTitle());
            descArea.setText(material.getDescription());
            fileNameField.setText(material.getFileName());
            filePathField.setText(material.getFilePath());
            
            for (int i = 0; i < subjectCombo.getItemCount(); i++) {
                if (subjectCombo.getItemAt(i).id == material.getSubjectId()) {
                    subjectCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void loadSubjects() {
        SubjectService ss = new SubjectService();
        List<Subject> subjects = ss.getAllSubjects();
        for (Subject s : subjects) {
            subjectCombo.addItem(new SubjectItem(s.getId(), s.getSubjectName()));
        }
    }
    
    private void save() {
        try {
            StudyMaterial m = material == null ? new StudyMaterial() : material;
            m.setTitle(titleField.getText().trim());
            
            SubjectItem selSubj = (SubjectItem) subjectCombo.getSelectedItem();
            if (selSubj != null) m.setSubjectId(selSubj.id);
            
            m.setDescription(descArea.getText().trim());
            m.setFileName(fileNameField.getText().trim());
            m.setFilePath(filePathField.getText().trim());
            
            if (material == null) {
                service.addStudyMaterial(m);
            } else {
                service.updateStudyMaterial(m);
            }
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    private static class SubjectItem {
        int id;
        String name;
        SubjectItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() { return name; }
    }
}
