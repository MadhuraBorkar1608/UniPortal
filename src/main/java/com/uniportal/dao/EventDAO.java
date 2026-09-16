package com.uniportal.dao;

import com.uniportal.model.Event;
import com.uniportal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public List<Event> getUpcomingEvents(String studentId, int deptId) {
        List<Event> list = new ArrayList<>();
        String query = "SELECT e.*, d.dept_name, " +
                       "(SELECT COUNT(*) FROM event_registrations r WHERE r.event_id = e.id AND r.student_id = ? AND r.status = 'REGISTERED') as is_reg " +
                       "FROM events e " +
                       "LEFT JOIN departments d ON e.dept_id = d.id " +
                       "WHERE e.status = 'PUBLISHED' AND e.event_date >= CURDATE() " +
                       "AND (e.dept_id IS NULL OR e.dept_id = ?) " +
                       "ORDER BY e.event_date, e.event_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, studentId);
            stmt.setInt(2, deptId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setEventName(rs.getString("event_name"));
                e.setDescription(rs.getString("description"));
                e.setEventDate(rs.getDate("event_date"));
                e.setEventTime(rs.getTime("event_time"));
                e.setVenue(rs.getString("venue"));
                e.setOrganizer(rs.getString("organizer"));
                e.setDeptId(rs.getInt("dept_id"));
                e.setRegistrationDeadline(rs.getTimestamp("registration_deadline"));
                e.setCapacity(rs.getInt("capacity"));
                e.setStatus(rs.getString("status"));
                e.setDeptName(rs.getString("dept_name"));
                e.setRegistered(rs.getInt("is_reg") > 0);
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean registerForEvent(int eventId, String studentId) {
        // check if already registered but cancelled, then update; else insert
        String checkQuery = "SELECT status FROM event_registrations WHERE event_id = ? AND student_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            
            boolean exists = false;
            boolean isCancelled = false;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, eventId);
                checkStmt.setString(2, studentId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    exists = true;
                    isCancelled = "CANCELLED".equals(rs.getString("status"));
                }
            }
            
            if (exists) {
                if (isCancelled) {
                    String updateQuery = "UPDATE event_registrations SET status = 'REGISTERED' WHERE event_id = ? AND student_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, eventId);
                        updateStmt.setString(2, studentId);
                        return updateStmt.executeUpdate() > 0;
                    }
                }
                return false; // already registered
            } else {
                String insertQuery = "INSERT INTO event_registrations (event_id, student_id, status) VALUES (?, ?, 'REGISTERED')";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, eventId);
                    insertStmt.setString(2, studentId);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        String query = "SELECT e.*, d.dept_name, 0 as is_reg " +
                       "FROM events e " +
                       "LEFT JOIN departments d ON e.dept_id = d.id " +
                       "ORDER BY e.event_date DESC, e.event_time DESC";
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

    public boolean addEvent(Event e) {
        String query = "INSERT INTO events (event_name, description, event_date, event_time, venue, organizer, dept_id, registration_deadline, capacity, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, e.getEventName());
            stmt.setString(2, e.getDescription());
            stmt.setDate(3, e.getEventDate());
            stmt.setTime(4, e.getEventTime());
            stmt.setString(5, e.getVenue());
            stmt.setString(6, e.getOrganizer());
            if (e.getDeptId() != null && e.getDeptId() > 0) stmt.setInt(7, e.getDeptId()); else stmt.setNull(7, Types.INTEGER);
            stmt.setTimestamp(8, e.getRegistrationDeadline());
            stmt.setInt(9, e.getCapacity());
            stmt.setString(10, e.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(Event e) {
        String query = "UPDATE events SET event_name=?, description=?, event_date=?, event_time=?, venue=?, organizer=?, dept_id=?, registration_deadline=?, capacity=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, e.getEventName());
            stmt.setString(2, e.getDescription());
            stmt.setDate(3, e.getEventDate());
            stmt.setTime(4, e.getEventTime());
            stmt.setString(5, e.getVenue());
            stmt.setString(6, e.getOrganizer());
            if (e.getDeptId() != null && e.getDeptId() > 0) stmt.setInt(7, e.getDeptId()); else stmt.setNull(7, Types.INTEGER);
            stmt.setTimestamp(8, e.getRegistrationDeadline());
            stmt.setInt(9, e.getCapacity());
            stmt.setString(10, e.getStatus());
            stmt.setInt(11, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(int id) {
        String query = "DELETE FROM events WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<com.uniportal.model.EventRegistration> getRegistrationsForEvent(int eventId) {
        List<com.uniportal.model.EventRegistration> list = new ArrayList<>();
        String query = "SELECT r.*, s.full_name as student_name, e.event_name " +
                       "FROM event_registrations r " +
                       "JOIN students s ON r.student_id = s.student_id " +
                       "JOIN events e ON r.event_id = e.id " +
                       "WHERE r.event_id = ? " +
                       "ORDER BY r.registration_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                com.uniportal.model.EventRegistration r = new com.uniportal.model.EventRegistration();
                r.setId(rs.getInt("id"));
                r.setEventId(rs.getInt("event_id"));
                r.setStudentId(rs.getString("student_id"));
                r.setRegistrationTime(rs.getTimestamp("registration_time"));
                r.setStatus(rs.getString("status"));
                r.setStudentName(rs.getString("student_name"));
                r.setEventName(rs.getString("event_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private Event mapEventRow(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setId(rs.getInt("id"));
        e.setEventName(rs.getString("event_name"));
        e.setDescription(rs.getString("description"));
        e.setEventDate(rs.getDate("event_date"));
        e.setEventTime(rs.getTime("event_time"));
        e.setVenue(rs.getString("venue"));
        e.setOrganizer(rs.getString("organizer"));
        e.setDeptId(rs.getInt("dept_id"));
        e.setRegistrationDeadline(rs.getTimestamp("registration_deadline"));
        e.setCapacity(rs.getInt("capacity"));
        e.setStatus(rs.getString("status"));
        e.setDeptName(rs.getString("dept_name"));
        e.setRegistered(rs.getInt("is_reg") > 0);
        return e;
    }
}
