package com.saucedemo.hooks;

import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import com.saucedemo.utils.ExtentReportManager;
import com.saucedemo.utils.ReportManager;
import com.saucedemo.config.ConfigReader;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class TestHooks {

    private WebDriver driver;

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());

        // Inicializar WebDriver
        String browser = ConfigReader.getBrowser();
        DriverManager.initializeDriver(browser);
        driver = DriverManager.getDriver();

        // Crear test en ExtentReport
        ExtentReportManager.createTest(scenario.getName(), getScenarioTags(scenario));

        // Navegar a la URL base
        String baseUrl = ConfigReader.getBaseUrl();
        DriverManager.navigateTo(baseUrl);

        ExtentReportManager.logInfo("Browser: " + browser);
        ExtentReportManager.logInfo("URL: " + baseUrl);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (driver != null) {
            // Capturar screenshot si el escenario falla
            if (scenario.isFailed()) {
                System.out.println("Scenario failed: " + scenario.getName());

                // Screenshot para Cucumber report
                String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
                if (base64Screenshot != null) {
                    byte[] screenshot = base64Screenshot.getBytes();
                    scenario.attach(screenshot, "image/png", "Screenshot");
                }

                // Screenshot para ExtentReport
                String screenshotPath = ScreenshotUtils.captureFailureScreenshot(driver, scenario.getName());
                ExtentReportManager.logFail("Test Failed: " + scenario.getName(), screenshotPath);
            } else {
                System.out.println("Scenario passed: " + scenario.getName());
                ExtentReportManager.logPass("Test Passed: " + scenario.getName());
            }

            // Cerrar WebDriver
            DriverManager.quitDriver();
        }

        ExtentReportManager.endTest();
        System.out.println("Finished scenario: " + scenario.getName());
    }

    // Hook que se ejecuta al final de todos los tests
    @io.cucumber.java.AfterAll
    public static void tearDownAll() {
        System.out.println("=== All Tests Completed ===");
        ReportManager.generateAllReports();
        ReportManager.cleanup();
    }

    @Before("@smoke")
    public void beforeSmokeTests(Scenario scenario) {
        ExtentReportManager.logInfo("This is a SMOKE test");
        System.out.println("Executing smoke test: " + scenario.getName());
    }

    @Before("@e2e")
    public void beforeE2ETests(Scenario scenario) {
        ExtentReportManager.logInfo("This is an END-TO-END test");
        System.out.println("Executing E2E test: " + scenario.getName());
    }

    @After("@critical")
    public void afterCriticalTests(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentReportManager.logFail("CRITICAL TEST FAILED - Immediate attention required!");
            System.err.println("CRITICAL TEST FAILED: " + scenario.getName());
        }
    }

    private String getScenarioTags(Scenario scenario) {
        return String.join(", ", scenario.getSourceTagNames());
    }
}