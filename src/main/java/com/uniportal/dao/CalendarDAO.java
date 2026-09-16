package com.uniportal.dao;

import com.uniportal.model.CalendarEvent;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarDAO {

    public List<CalendarEvent> getAllEvents() {
        List<CalendarEvent> list = new ArrayList<>();
        String query = "SELECT * FROM academic_calendar ORDER BY event_date ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addEvent(CalendarEvent e) {
        String query = "INSERT INTO academic_calendar (event_date, event_type, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, e.getEventDate());
            stmt.setString(2, e.getEventType());
            stmt.setString(3, e.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(CalendarEvent e) {
        String query = "UPDATE academic_calendar SET event_date=?, event_type=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, e.getEventDate());
            stmt.setString(2, e.getEventType());
            stmt.setString(3, e.getDescription());
            stmt.setInt(4, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(int id) {
        String query = "DELETE FROM academic_calendar WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private CalendarEvent mapRow(ResultSet rs) throws SQLException {
        CalendarEvent e = new CalendarEvent();
        e.setId(rs.getInt("id"));
        e.setEventDate(rs.getDate("event_date"));
        e.setEventType(rs.getString("event_type"));
        e.setDescription(rs.getString("description"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
