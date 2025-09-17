package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.config.ConfigReader;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFReportGenerator {

    private static final String PDF_REPORT_DIR = "test-output/pdf-reports/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    static {
        createReportDirectory();
    }

    private static void createReportDirectory() {
        File directory = new File(PDF_REPORT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String generatePDFReport() {
        try {
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String pdfReportPath = PDF_REPORT_DIR + "SauceDemoReport_" + timestamp + ".pdf";

            // Create ExtentReports for PDF
            ExtentReports pdfExtent = new ExtentReports();

            // Note: ExtentReports 5.x doesn't have native PDF support
            // This is a placeholder for PDF generation logic
            // You would need to implement PDF conversion using libraries like:
            // - iText PDF
            // - Flying Saucer
            // - wkhtmltopdf

            System.out.println("PDF Report generation initiated...");
            System.out.println("PDF Report would be generated at: " + pdfReportPath);

            // For now, we'll create an HTML report that can be converted to PDF
            String htmlToPdfPath = generateHTMLForPDFConversion();

            return htmlToPdfPath;

        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            return null;
        }
    }

    private static String generateHTMLForPDFConversion() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String htmlReportPath = PDF_REPORT_DIR + "SauceDemoReport_ForPDF_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(htmlReportPath);

        // Configure for PDF-friendly HTML
        sparkReporter.config().setReportName("SauceDemo Automation Test Results");
        sparkReporter.config().setDocumentTitle("Test Execution Report - PDF Version");
        sparkReporter.config().setEncoding("utf-8");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information
        extent.setSystemInfo("Report Type", "PDF Export");
        extent.setSystemInfo("Generated On", LocalDateTime.now().toString());
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Base URL", ConfigReader.getBaseUrl());

        extent.flush();

        System.out.println("HTML report for PDF conversion created: " + htmlReportPath);
        System.out.println("To convert to PDF, use tools like:");
        System.out.println("1. wkhtmltopdf " + htmlReportPath + " output.pdf");
        System.out.println("2. Chrome --headless --print-to-pdf=output.pdf " + htmlReportPath);

        return htmlReportPath;
    }

    // Method to integrate with popular PDF libraries
    public static void generatePDFUsingHTMLToPDF(String htmlPath, String pdfPath) {
        // This method would integrate with libraries like:
        // - Flying Saucer + iText
        // - wkhtmltopdf wrapper
        // - Puppeteer (via Node.js integration)

        System.out.println("PDF conversion from HTML: " + htmlPath + " -> " + pdfPath);
        System.out.println("Implementation depends on chosen PDF library");
    }
}