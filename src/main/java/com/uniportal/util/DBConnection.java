package com.uniportal.util;

import com.uniportal.config.DatabaseConfig;
import com.uniportal.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }
    
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database. Please verify configuration.", e);
        }
    }
}
