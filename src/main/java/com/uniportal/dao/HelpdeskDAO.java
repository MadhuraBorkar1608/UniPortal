package com.uniportal.dao;

import com.uniportal.model.HelpdeskTicket;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpdeskDAO {

    public List<HelpdeskTicket> getTicketsByStudent(String studentId) {
        List<HelpdeskTicket> list = new ArrayList<>();
        String query = "SELECT * FROM helpdesk_tickets WHERE student_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                HelpdeskTicket t = new HelpdeskTicket();
                t.setTicketId(rs.getString("ticket_id"));
                t.setStudentId(rs.getString("student_id"));
                t.setCategory(rs.getString("category"));
                t.setSubject(rs.getString("subject"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<HelpdeskTicket> getAllTickets() {
        List<HelpdeskTicket> list = new ArrayList<>();
        String query = "SELECT * FROM helpdesk_tickets ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                HelpdeskTicket t = new HelpdeskTicket();
                t.setTicketId(rs.getString("ticket_id"));
                t.setStudentId(rs.getString("student_id"));
                t.setCategory(rs.getString("category"));
                t.setSubject(rs.getString("subject"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean raiseTicket(HelpdeskTicket t) {
        String query = "INSERT INTO helpdesk_tickets (ticket_id, student_id, category, subject, description, status) VALUES (?, ?, ?, ?, ?, 'OPEN')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, t.getTicketId());
            stmt.setString(2, t.getStudentId());
            stmt.setString(3, t.getCategory());
            stmt.setString(4, t.getSubject());
            stmt.setString(5, t.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTicketStatus(String ticketId, String status) {
        String query = "UPDATE helpdesk_tickets SET status = ? WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReply(com.uniportal.model.TicketReply reply) {
        String query = "INSERT INTO ticket_replies (ticket_id, reply_text, replied_by_user_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, reply.getTicketId());
            stmt.setString(2, reply.getReplyText());
            stmt.setInt(3, reply.getRepliedByUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<com.uniportal.model.TicketReply> getRepliesForTicket(String ticketId) {
        List<com.uniportal.model.TicketReply> list = new ArrayList<>();
        String query = "SELECT r.*, u.username as replier_name FROM ticket_replies r " +
                       "JOIN users u ON r.replied_by_user_id = u.id " +
                       "WHERE r.ticket_id = ? ORDER BY r.reply_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                com.uniportal.model.TicketReply r = new com.uniportal.model.TicketReply();
                r.setId(rs.getInt("id"));
                r.setTicketId(rs.getString("ticket_id"));
                r.setReplyText(rs.getString("reply_text"));
                r.setRepliedByUserId(rs.getInt("replied_by_user_id"));
                r.setReplyTime(rs.getTimestamp("reply_time"));
                r.setReplierName(rs.getString("replier_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
