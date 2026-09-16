package com.uniportal.ui.admin.helpdesk;

import com.uniportal.model.HelpdeskTicket;
import com.uniportal.service.HelpdeskService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HelpdeskManagementPanel extends JPanel {

    private HelpdeskService service;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<HelpdeskTicket> ticketList;

    public HelpdeskManagementPanel() {
        service = new HelpdeskService();
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Helpdesk Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Ticket ID", "Student ID", "Category", "Subject", "Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        StyledButton viewBtn = new StyledButton("View / Reply");
        
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                HelpdeskTicket ticket = ticketList.get(row);
                TicketDetailsDialog dialog = new TicketDetailsDialog(SwingUtilities.getWindowAncestor(this), ticket, service, true);
                dialog.setVisible(true);
                loadData();
            } else {
                UIUtils.showError(this, "Please select a ticket.");
            }
        });

        bottomPanel.add(viewBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        ticketList = service.getAllTickets();
        for (HelpdeskTicket t : ticketList) {
            tableModel.addRow(new Object[]{
                t.getTicketId(), 
                t.getStudentId(), 
                t.getCategory(), 
                t.getSubject(), 
                t.getStatus(), 
                t.getCreatedAt()
            });
        }
    }
}
