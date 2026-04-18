package com.anudip.tracker.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties PROPERTIES = loadProperties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("MySQL JDBC driver not found.", ex);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getConfig("db.url", "DB_URL");
        String username = getConfig("db.username", "DB_USERNAME");
        String password = getConfig("db.password", "DB_PASSWORD");
        return DriverManager.getConnection(url, username, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load " + PROPERTIES_FILE, ex);
        }

        properties.putIfAbsent("db.url", "jdbc:mysql://127.0.0.1:3306/freelancer_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        properties.putIfAbsent("db.username", "tracker");
        properties.putIfAbsent("db.password", "tracker123");
        return properties;
    }

    private static String getConfig(String propertyKey, String envKey) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }
        return PROPERTIES.getProperty(propertyKey);
    }
}
