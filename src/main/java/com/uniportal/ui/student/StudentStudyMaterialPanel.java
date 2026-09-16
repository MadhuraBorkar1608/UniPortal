package com.uniportal.ui.student;

import com.uniportal.model.Student;
import com.uniportal.model.StudyMaterial;
import com.uniportal.service.StudentService;
import com.uniportal.service.StudyMaterialService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentStudyMaterialPanel extends JPanel {

    private StudyMaterialService materialService;
    private StudentService studentService;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<StudyMaterial> materialList;

    public StudentStudyMaterialPanel() {
        materialService = new StudyMaterialService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Study Material");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Subject", "Title", "Description", "Uploaded On", "File"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        JButton downloadBtn = new JButton("View File Details");
        downloadBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                StudyMaterial m = materialList.get(row);
                UIUtils.showSuccess(this, "File: " + m.getFileName() + "\nPath: " + m.getFilePath() + "\n\n(Simulated download)");
            } else {
                UIUtils.showError(this, "Please select a material first.");
            }
        });
        bottomPanel.add(downloadBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                materialList = materialService.getStudentMaterials(s.getCourseId(), s.getCurrentSemester());
                for (StudyMaterial m : materialList) {
                    tableModel.addRow(new Object[]{
                        m.getSubjectName(), 
                        m.getTitle(), 
                        m.getDescription(),
                        m.getUploadDate().toString(), 
                        m.getFileName()
                    });
                }
            }
        }
    }
}
