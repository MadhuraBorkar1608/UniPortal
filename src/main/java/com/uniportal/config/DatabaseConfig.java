package com.uniportal.config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = DatabaseConfig.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                properties.load(is);
            } else {
                // Fallback defaults for demo purposes
                properties.setProperty("db.url", "jdbc:mysql://localhost:3306/college_management_system");
                properties.setProperty("db.user", "root");
                properties.setProperty("db.password", "maddiemysql@1516");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/college_management_system");
    }

    public static String getUser() {
        return properties.getProperty("db.user", "root");
    }

    public static String getPassword() {
        return properties.getProperty("db.password", "");
    }
}
