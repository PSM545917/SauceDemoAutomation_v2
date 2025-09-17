package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = ConfigReader.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                // Configuración por defecto si no existe el archivo
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("base.url", "https://www.saucedemo.com/");
        properties.setProperty("browser", "chrome");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("page.load.timeout", "30");
        properties.setProperty("report.title", "SauceDemo Automation Report");
        properties.setProperty("report.name", "Test Execution Report");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return System.getProperty("browser", getProperty("browser"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    public static String getReportTitle() {
        return getProperty("report.title");
    }

    public static String getReportName() {
        return getProperty("report.name");
    }
}