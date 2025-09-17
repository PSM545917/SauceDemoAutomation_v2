package com.saucedemo.steps;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ExtentReportManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private CommonSteps commonSteps;

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
        this.commonSteps = new CommonSteps();
    }

    @When("I enter username {string} and password {string}")
    public void i_enter_username_and_password(String username, String password) {
        loginPage = commonSteps.getLoginPage();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        ExtentReportManager.logInfo("Entered credentials - Username: " + username);
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        loginPage = commonSteps.getLoginPage();
        loginPage.clickLoginButton();
        ExtentReportManager.logInfo("Clicked login button");
    }

    @Then("I should see {string} as the page title")
    public void i_should_see_as_the_page_title(String expectedTitle) {
        var inventoryPage = commonSteps.getInventoryPage();
        String actualTitle = inventoryPage.getPageTitle();
        Assertions.assertEquals(expectedTitle, actualTitle,
                "Page title should match expected value");
        ExtentReportManager.logPass("Page title verified: " + expectedTitle);
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessage) {
        loginPage = commonSteps.getLoginPage();
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed");

        String actualErrorMessage = loginPage.getErrorMessage();
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage,
                "Error message should match expected value");
        ExtentReportManager.logPass("Error message verified: " + expectedErrorMessage);
    }

    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        loginPage = commonSteps.getLoginPage();
        Assertions.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should remain on login page");
        ExtentReportManager.logPass("Remained on login page as expected");
    }
}