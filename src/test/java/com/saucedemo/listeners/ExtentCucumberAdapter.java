package com.saucedemo.listeners;

import com.saucedemo.utils.ExtentReportManager;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

public class ExtentCucumberAdapter implements EventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::caseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::caseFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void runStarted(TestRunStarted event) {
        System.out.println("=== Test Execution Started ===");
    }

    private void runFinished(TestRunFinished event) {
        ExtentReportManager.generateReports();
        System.out.println("=== Test Execution Completed ===");
    }

    private void caseStarted(TestCaseStarted event) {
        String featureName = event.getTestCase().getUri().toString();
        String scenarioName = event.getTestCase().getName();
        ExtentReportManager.createTest(scenarioName, "Feature: " + featureName);
    }

    private void caseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();

        switch (result.getStatus()) {
            case PASSED:
                ExtentReportManager.logPass("Scenario Passed: " + testCase.getName());
                break;
            case FAILED:
                ExtentReportManager.logFail("Scenario Failed: " + testCase.getName());
                if (DriverManager.getDriver() != null) {
                    String screenshotPath = ScreenshotUtils.captureFailureScreenshot(
                            DriverManager.getDriver(), testCase.getName());
                    if (screenshotPath != null) {
                        ExtentReportManager.logFail("Screenshot captured", screenshotPath);
                    }
                }
                break;
            case SKIPPED:
                ExtentReportManager.logSkip("Scenario Skipped: " + testCase.getName());
                break;
            case PENDING:
                ExtentReportManager.logWarning("Scenario Pending: " + testCase.getName());
                break;
        }

        ExtentReportManager.endTest();
    }

    private void stepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            String stepText = step.getStep().getText();
            ExtentReportManager.logInfo("Executing Step: " + stepText);
        }
    }

    private void stepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            String stepText = step.getStep().getText();
            Result result = event.getResult();

            switch (result.getStatus()) {
                case PASSED:
                    ExtentReportManager.logPass("Step Passed: " + stepText);
                    break;
                case FAILED:
                    ExtentReportManager.logFail("Step Failed: " + stepText +
                            " | Error: " + result.getError().getMessage());
                    break;
                case SKIPPED:
                    ExtentReportManager.logSkip("Step Skipped: " + stepText);
                    break;
            }
        }
    }
}