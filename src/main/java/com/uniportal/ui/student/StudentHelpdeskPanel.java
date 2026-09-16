package com.uniportal.ui.student;

import com.uniportal.model.HelpdeskTicket;
import com.uniportal.model.Student;
import com.uniportal.service.HelpdeskService;
import com.uniportal.service.StudentService;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;
import com.uniportal.util.ValidationUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentHelpdeskPanel extends JPanel {

    private HelpdeskService helpdeskService;
    private StudentService studentService;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentHelpdeskPanel() {
        helpdeskService = new HelpdeskService();
        studentService = new StudentService();
        
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Helpdesk & Support");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Ticket ID", "Category", "Subject", "Status", "Date"};
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
        JButton raiseBtn = new JButton("Raise New Ticket");
        raiseBtn.addActionListener(e -> showRaiseTicketDialog());
        
        JButton viewBtn = new JButton("View / Reply");
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                HelpdeskTicket ticket = getTicketFromRow(row);
                com.uniportal.ui.admin.helpdesk.TicketDetailsDialog dialog = new com.uniportal.ui.admin.helpdesk.TicketDetailsDialog(
                    SwingUtilities.getWindowAncestor(this), ticket, helpdeskService, false);
                dialog.setVisible(true);
                loadData();
            } else {
                UIUtils.showError(this, "Please select a ticket.");
            }
        });
        
        bottomPanel.add(raiseBtn);
        bottomPanel.add(viewBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private HelpdeskTicket getTicketFromRow(int row) {
        String ticketId = (String) tableModel.getValueAt(row, 0);
        Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
        if (s != null) {
            List<HelpdeskTicket> tickets = helpdeskService.getMyTickets(s.getStudentId());
            for (HelpdeskTicket t : tickets) {
                if (t.getTicketId().equals(ticketId)) return t;
            }
        }
        return null;
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        if (SessionManager.isAuthenticated()) {
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            if (s != null) {
                List<HelpdeskTicket> tickets = helpdeskService.getMyTickets(s.getStudentId());
                for (HelpdeskTicket t : tickets) {
                    tableModel.addRow(new Object[]{
                        t.getTicketId(), 
                        t.getCategory(), 
                        t.getSubject(),
                        t.getStatus(),
                        t.getCreatedAt().toString()
                    });
                }
            }
        }
    }
    
    private void showRaiseTicketDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Raise Ticket", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"IT Support", "Administration", "Academic", "Hostel"});
        dialog.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Subject:"), gbc);
        
        gbc.gridx = 1;
        JTextField subjectField = new JTextField();
        dialog.add(subjectField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        dialog.add(new JScrollPane(descArea), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> {
            if (subjectField.getText().trim().isEmpty() || descArea.getText().trim().isEmpty()) {
                UIUtils.showError(dialog, "Please fill in all fields.");
                return;
            }
            Student s = studentService.getStudentProfile(SessionManager.getCurrentUser().getId());
            boolean ok = helpdeskService.raiseTicket(s.getStudentId(), categoryCombo.getSelectedItem().toString(), subjectField.getText().trim(), descArea.getText().trim());
            if (ok) {
                UIUtils.showSuccess(dialog, "Ticket raised successfully.");
                dialog.dispose();
                loadData();
            } else {
                UIUtils.showError(dialog, "Failed to raise ticket.");
            }
        });
        dialog.add(submitBtn, gbc);
        
        dialog.setVisible(true);
    }
}
