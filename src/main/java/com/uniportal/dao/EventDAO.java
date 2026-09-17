package com.uniportal.dao;

import com.uniportal.model.CalendarEvent;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public List<CalendarEvent> getAllEvents() {
        List<CalendarEvent> list = new ArrayList<>();
        String query = "SELECT * FROM academic_calendar ORDER BY event_date DESC, event_time DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapEventRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CalendarEvent> getUpcomingEvents(String studentId, int deptId) {
        List<CalendarEvent> list = new ArrayList<>();
        String query = "SELECT * FROM academic_calendar WHERE status = 'SCHEDULED' AND event_date >= CURRENT_DATE AND (dept_id IS NULL OR dept_id = ?) ORDER BY event_date, event_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, deptId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapEventRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addEvent(CalendarEvent e) {
        String query = "INSERT INTO academic_calendar (title, description, event_date, event_time, event_type, duration_minutes, subject_id, dept_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, e.getTitle());
            stmt.setString(2, e.getDescription());
            stmt.setDate(3, e.getEventDate());
            stmt.setTime(4, e.getEventTime());
            stmt.setString(5, e.getEventType());
            stmt.setInt(6, e.getDurationMinutes());
            if (e.getSubjectId() > 0) stmt.setInt(7, e.getSubjectId()); else stmt.setNull(7, Types.INTEGER);
            if (e.getDeptId() > 0) stmt.setInt(8, e.getDeptId()); else stmt.setNull(8, Types.INTEGER);
            stmt.setString(9, e.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(CalendarEvent e) {
        String query = "UPDATE academic_calendar SET title=?, description=?, event_date=?, event_time=?, event_type=?, duration_minutes=?, subject_id=?, dept_id=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, e.getTitle());
            stmt.setString(2, e.getDescription());
            stmt.setDate(3, e.getEventDate());
            stmt.setTime(4, e.getEventTime());
            stmt.setString(5, e.getEventType());
            stmt.setInt(6, e.getDurationMinutes());
            if (e.getSubjectId() > 0) stmt.setInt(7, e.getSubjectId()); else stmt.setNull(7, Types.INTEGER);
            if (e.getDeptId() > 0) stmt.setInt(8, e.getDeptId()); else stmt.setNull(8, Types.INTEGER);
            stmt.setString(9, e.getStatus());
            stmt.setInt(10, e.getId());
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private CalendarEvent mapEventRow(ResultSet rs) throws SQLException {
        CalendarEvent e = new CalendarEvent();
        e.setId(rs.getInt("id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setEventDate(rs.getDate("event_date"));
        e.setEventTime(rs.getTime("event_time"));
        e.setEventType(rs.getString("event_type"));
        e.setDurationMinutes(rs.getInt("duration_minutes"));
        e.setSubjectId(rs.getInt("subject_id"));
        e.setDeptId(rs.getInt("dept_id"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}
