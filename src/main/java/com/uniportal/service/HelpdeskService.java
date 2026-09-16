package com.uniportal.service;

import com.uniportal.dao.HelpdeskDAO;
import com.uniportal.model.HelpdeskTicket;
import java.util.List;
import java.util.UUID;

public class HelpdeskService {
    private HelpdeskDAO dao;
    
    public HelpdeskService() {
        this.dao = new HelpdeskDAO();
    }
    
    public List<HelpdeskTicket> getMyTickets(String studentId) {
        return dao.getTicketsByStudent(studentId);
    }
    
    public List<HelpdeskTicket> getAllTickets() {
        return dao.getAllTickets();
    }

    public boolean raiseTicket(String studentId, String category, String subject, String description) {
        HelpdeskTicket t = new HelpdeskTicket();
        t.setTicketId("HD" + System.currentTimeMillis());
        t.setStudentId(studentId);
        t.setCategory(category);
        t.setSubject(subject);
        t.setDescription(description);
        return dao.raiseTicket(t);
    }
    
    public boolean updateTicketStatus(String ticketId, String status) {
        return dao.updateTicketStatus(ticketId, status);
    }
    
    public boolean addReply(String ticketId, String replyText, int repliedByUserId) {
        com.uniportal.model.TicketReply reply = new com.uniportal.model.TicketReply();
        reply.setTicketId(ticketId);
        reply.setReplyText(replyText);
        reply.setRepliedByUserId(repliedByUserId);
        return dao.addReply(reply);
    }
    
    public List<com.uniportal.model.TicketReply> getRepliesForTicket(String ticketId) {
        return dao.getRepliesForTicket(ticketId);
    }
}
