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
                properties.setProperty("db.url", "jdbc:h2:./uniportal_db;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
                properties.setProperty("db.user", "sa");
                properties.setProperty("db.password", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url", "jdbc:h2:./uniportal_db;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
    }

    public static String getUser() {
        return properties.getProperty("db.user", "root");
    }

    public static String getPassword() {
        return properties.getProperty("db.password", "");
    }
}
