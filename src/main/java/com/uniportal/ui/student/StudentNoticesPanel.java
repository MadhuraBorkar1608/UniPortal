package com.uniportal.ui.student;

import com.uniportal.model.Notice;
import com.uniportal.model.Student;
import com.uniportal.service.NoticeService;
import com.uniportal.service.StudentService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentNoticesPanel extends JPanel {

    private NoticeService noticeService;
    private StudentService studentService;
    
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentNoticesPanel() {
        noticeService = new NoticeService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Notices");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Category", "Date", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    String title = (String) tableModel.getValueAt(modelRow, 1);
                    String desc = (String) tableModel.getValueAt(modelRow, 4);
                    JOptionPane.showMessageDialog(StudentNoticesPanel.this,
                        desc,
                        title,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                // Get general notices + department specific notices
                List<Notice> notices = noticeService.getPublishedNotices(s.getDeptId());
                for (Notice n : notices) {
                    tableModel.addRow(new Object[]{
                        n.getId(), n.getTitle(), n.getCategory(), n.getPublishDate(), n.getDescription()
                    });
                }
            }
        }
    }
}
