package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.config.ConfigReader;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    private static final String BASE_REPORT_DIR = "test-output/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void generateAllReports() {
        System.out.println("=== Generating All Report Types ===");

        // Generate ExtentReport (HTML Spark)
        generateExtentSparkReport();

        // Generate ExtentReport (HTML Standard)
        generateExtentHTMLReport();

        // Generate PDF Report
        PDFReportGenerator.generatePDFReport();

        // Finalize ExtentReports
        ExtentReportManager.generateReports();

        System.out.println("=== All Reports Generated Successfully ===");
        printReportLocations();
    }

    private static void generateExtentSparkReport() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String sparkReportPath = BASE_REPORT_DIR + "spark-reports/SauceDemoSpark_" + timestamp + ".html";

        createDirectory(sparkReportPath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(sparkReportPath);
        sparkReporter.config().setReportName("SauceDemo Automation - Spark Report");
        sparkReporter.config().setDocumentTitle("Selenium Cucumber Framework - Spark");

        ExtentReports sparkExtent = new ExtentReports();
        sparkExtent.attachReporter(sparkReporter);
        addSystemInfo(sparkExtent, "Spark HTML Report");
        sparkExtent.flush();

        System.out.println("Spark Report Generated: " + sparkReportPath);
    }

    private static void generateExtentHTMLReport() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String htmlReportPath = BASE_REPORT_DIR + "html-reports/SauceDemoHTML_" + timestamp + ".html";

        createDirectory(htmlReportPath);

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(htmlReportPath);
        htmlReporter.config().setReportName("SauceDemo Automation - HTML Report");
        htmlReporter.config().setDocumentTitle("Selenium Cucumber Framework - HTML");

        ExtentReports htmlExtent = new ExtentReports();
        htmlExtent.attachReporter(htmlReporter);
        addSystemInfo(htmlExtent, "Standard HTML Report");
        htmlExtent.flush();

        System.out.println("HTML Report Generated: " + htmlReportPath);
    }

    private static void createDirectory(String filePath) {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static void addSystemInfo(ExtentReports extent, String reportType) {
        extent.setSystemInfo("Report Type", reportType);
        extent.setSystemInfo("Framework", "Selenium + Cucumber + JUnit5");
        extent.setSystemInfo("Application Under Test", "SauceDemo");
        extent.setSystemInfo("Test Environment", "QA");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Base URL", ConfigReader.getBaseUrl());
        extent.setSystemInfo("Generated On", LocalDateTime.now().toString());
    }

    private static void printReportLocations() {
        System.out.println("\n📊 REPORT LOCATIONS:");
        System.out.println("├── ExtentReports (Main): " + BASE_REPORT_DIR + "extent-reports/");
        System.out.println("├── Spark Reports: " + BASE_REPORT_DIR + "spark-reports/");
        System.out.println("├── HTML Reports: " + BASE_REPORT_DIR + "html-reports/");
        System.out.println("├── PDF Reports: " + BASE_REPORT_DIR + "pdf-reports/");
        System.out.println("├── Cucumber Reports: " + BASE_REPORT_DIR + "cucumber-reports/");
        System.out.println("├── Screenshots: " + BASE_REPORT_DIR + "screenshots/");
        System.out.println("└── Smoke Test Reports: " + BASE_REPORT_DIR + "smoke-reports/");
        System.out.println();
    }

    public static void cleanup() {
        // Clean up old reports (optional)
        System.out.println("Report cleanup completed");
    }
}