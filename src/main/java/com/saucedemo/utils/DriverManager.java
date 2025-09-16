package com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String DEFAULT_BROWSER = "chrome";
    private static final int DEFAULT_TIMEOUT = 10;

    public static void initializeDriver() {
        initializeDriver(DEFAULT_BROWSER);
    }

    public static void initializeDriver(String browserName) {
        if (driver.get() == null) {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    setupChromeDriver();
                    break;
                case "firefox":
                    setupFirefoxDriver();
                    break;
                case "edge":
                    setupEdgeDriver();
                    break;
                case "headless-chrome":
                    setupHeadlessChromeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browserName);
            }

            configureDriver();
        }
    }

    private static void setupChromeDriver() {
        // Selenium 4.6+ maneja automáticamente los drivers
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        driver.set(new ChromeDriver(options));
    }

    private static void setupHeadlessChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        driver.set(new ChromeDriver(options));
    }

    private static void setupFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        driver.set(new FirefoxDriver(options));
    }

    private static void setupEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        driver.set(new EdgeDriver(options));
    }

    private static void configureDriver() {
        WebDriver webDriver = driver.get();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        webDriver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
        }
    }

    public static void navigateTo(String url) {
        getDriver().get(url);
    }
}