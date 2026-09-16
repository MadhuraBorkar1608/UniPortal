package com.uniportal.model;

import java.sql.Timestamp;

public class TicketReply {
    private int id;
    private String ticketId;
    private String replyText;
    private int repliedByUserId;
    private Timestamp replyTime;

    // Derived
    private String replierName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public int getRepliedByUserId() { return repliedByUserId; }
    public void setRepliedByUserId(int repliedByUserId) { this.repliedByUserId = repliedByUserId; }

    public Timestamp getReplyTime() { return replyTime; }
    public void setReplyTime(Timestamp replyTime) { this.replyTime = replyTime; }

    public String getReplierName() { return replierName; }
    public void setReplierName(String replierName) { this.replierName = replierName; }
}
