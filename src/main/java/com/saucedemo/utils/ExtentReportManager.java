package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.saucedemo.config.ConfigReader;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String REPORT_DIR = "test-output/extent-reports/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    static {
        createReportDirectory();
    }

    private static void createReportDirectory() {
        File directory = new File(REPORT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String reportPath = REPORT_DIR + "SauceDemoReport_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            configureSparkReporter(sparkReporter);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            setSystemInfo();
        }
        return extent;
    }

    private static void configureSparkReporter(ExtentSparkReporter sparkReporter) {
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName(ConfigReader.getReportName());
        sparkReporter.config().setDocumentTitle(ConfigReader.getReportTitle());
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setEncoding("utf-8");
    }

    private static void setSystemInfo() {
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Base URL", ConfigReader.getBaseUrl());
    }

    public static synchronized void createTest(String testName, String description) {
        ExtentTest extentTest = getExtentReports().createTest(testName, description);
        test.set(extentTest);
    }

    public static synchronized void logPass(String message) {
        if (test.get() != null) {
            test.get().log(Status.PASS, message);
        }
    }

    public static synchronized void logFail(String message) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
        }
    }

    public static synchronized void logFail(String message, String screenshotPath) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
            if (screenshotPath != null) {
                try {
                    test.get().addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    test.get().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
                }
            }
        }
    }

    public static synchronized void logInfo(String message) {
        if (test.get() != null) {
            test.get().log(Status.INFO, message);
        }
    }

    public static synchronized void logWarning(String message) {
        if (test.get() != null) {
            test.get().log(Status.WARNING, message);
        }
    }

    public static synchronized void logSkip(String message) {
        if (test.get() != null) {
            test.get().log(Status.SKIP, message);
        }
    }

    public static synchronized void endTest() {
        test.remove();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    // Method to create different types of reports
    public static void generateReports() {
        flush();
        System.out.println("ExtentReports generated successfully!");
        System.out.println("Report location: " + REPORT_DIR);
    }
}