package com.uniportal.util;

import com.uniportal.config.DatabaseConfig;
import com.uniportal.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    
    static {
        try {
            Class.forName("org.h2.Driver");
            initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load H2 JDBC Driver", e);
        }
    }
    
    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUser(), DatabaseConfig.getPassword())) {
            boolean initNeeded = true;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "DEPARTMENTS", null)) {
                if (rs.next()) {
                    initNeeded = false;
                }
            }
            if (initNeeded) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("RUNSCRIPT FROM 'classpath:init_db.sql'");
                }
            }
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUser(), DatabaseConfig.getPassword());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database. Please verify configuration.", e);
        }
    }
}
