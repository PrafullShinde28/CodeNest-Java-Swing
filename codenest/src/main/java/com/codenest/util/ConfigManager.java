
package com.codenest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "application.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                // Default properties if file not found
                setDefaultProperties();
            }
        } catch (IOException e) {
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/codenest_db");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "manager");
        properties.setProperty("app.title", "CodeNest - Student Service Platform");
        properties.setProperty("app.version", "1.0.0");
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}