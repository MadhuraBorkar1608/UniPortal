package com.uniportal.ui.admin.helpdesk;

import com.uniportal.model.HelpdeskTicket;
import com.uniportal.model.TicketReply;
import com.uniportal.service.HelpdeskService;
import com.uniportal.ui.common.StyledButton;
import com.uniportal.util.SessionManager;
import com.uniportal.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicketDetailsDialog extends JDialog {

    private HelpdeskTicket ticket;
    private HelpdeskService service;
    private boolean isAdmin;
    
    private JPanel repliesPanel;
    private JComboBox<String> statusCombo;

    public TicketDetailsDialog(Window owner, HelpdeskTicket ticket, HelpdeskService service, boolean isAdmin) {
        super(owner, "Ticket Details - " + ticket.getTicketId(), ModalityType.APPLICATION_MODAL);
        this.ticket = ticket;
        this.service = service;
        this.isAdmin = isAdmin;
        
        setSize(600, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        // Top section: Ticket Info
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("<html><b>Ticket ID:</b> " + ticket.getTicketId() + "</html>"));
        infoPanel.add(new JLabel("<html><b>Student ID:</b> " + ticket.getStudentId() + "</html>"));
        infoPanel.add(new JLabel("<html><b>Subject:</b> " + ticket.getSubject() + "</html>"));
        infoPanel.add(new JLabel("<html><b>Description:</b> " + ticket.getDescription() + "</html>"));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("<html><b>Status:</b></html>"));
        if (isAdmin) {
            statusCombo = new JComboBox<>(new String[]{"OPEN", "IN PROGRESS", "RESOLVED", "CLOSED"});
            statusCombo.setSelectedItem(ticket.getStatus());
            StyledButton updateStatusBtn = new StyledButton("Update Status");
            updateStatusBtn.addActionListener(e -> {
                String newStatus = (String) statusCombo.getSelectedItem();
                service.updateTicketStatus(ticket.getTicketId(), newStatus);
                ticket.setStatus(newStatus);
                UIUtils.showSuccess(this, "Status updated!");
            });
            statusPanel.add(statusCombo);
            statusPanel.add(updateStatusBtn);
        } else {
            statusPanel.add(new JLabel(ticket.getStatus()));
        }
        infoPanel.add(statusPanel);
        
        add(infoPanel, BorderLayout.NORTH);
        
        // Middle section: Replies
        repliesPanel = new JPanel();
        repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(repliesPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Replies"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom section: Add reply
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea replyArea = new JTextArea(4, 40);
        replyArea.setLineWrap(true);
        bottomPanel.add(new JScrollPane(replyArea), BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StyledButton sendBtn = new StyledButton("Send Reply");
        StyledButton closeBtn = new StyledButton("Close");
        
        sendBtn.addActionListener(e -> {
            if (replyArea.getText().trim().isEmpty()) {
                UIUtils.showError(this, "Reply cannot be empty.");
                return;
            }
            int currentUserId = SessionManager.getCurrentUser().getId();
            service.addReply(ticket.getTicketId(), replyArea.getText().trim(), currentUserId);
            replyArea.setText("");
            loadReplies();
        });
        
        closeBtn.addActionListener(e -> dispose());
        
        btnPanel.add(sendBtn);
        btnPanel.add(closeBtn);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadReplies();
    }
    
    private void loadReplies() {
        repliesPanel.removeAll();
        List<TicketReply> replies = service.getRepliesForTicket(ticket.getTicketId());
        for (TicketReply r : replies) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            JLabel header = new JLabel("<html><b>" + r.getReplierName() + "</b> at " + r.getReplyTime() + "</html>");
            header.setForeground(UIUtils.COLOR_PRIMARY);
            JLabel body = new JLabel("<html>" + r.getReplyText().replace("\n", "<br>") + "</html>");
            p.add(header, BorderLayout.NORTH);
            p.add(body, BorderLayout.CENTER);
            repliesPanel.add(p);
        }
        repliesPanel.revalidate();
        repliesPanel.repaint();
    }
}
