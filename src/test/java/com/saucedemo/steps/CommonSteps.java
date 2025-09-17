package com.saucedemo.steps;

import com.saucedemo.pages.*;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ExtentReportManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class CommonSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    public CommonSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Given("I am on the SauceDemo login page")
    public void i_am_on_the_sauce_demo_login_page() {
        loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        ExtentReportManager.logPass("Successfully navigated to SauceDemo login page");
    }

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        loginPage = new LoginPage(driver);
        inventoryPage = loginPage.login(username, password);
        ExtentReportManager.logInfo("Attempting login with username: " + username);
    }

    @Then("I should be on the inventory page")
    public void i_should_be_on_the_inventory_page() {
        inventoryPage = new InventoryPage(driver);
        Assertions.assertTrue(inventoryPage.isInventoryPageDisplayed(),
                "Should be redirected to inventory page");
        ExtentReportManager.logPass("Successfully redirected to inventory page");
    }

    @Then("I should be redirected to the inventory page")
    public void i_should_be_redirected_to_the_inventory_page() {
        i_should_be_on_the_inventory_page();
    }

    @When("I go to the shopping cart")
    public void i_go_to_the_shopping_cart() {
        inventoryPage = new InventoryPage(driver);
        cartPage = inventoryPage.goToCart();
        ExtentReportManager.logInfo("Navigated to shopping cart");
    }

    @When("I logout from the application")
    public void i_logout_from_the_application() {
        inventoryPage = new InventoryPage(driver);
        loginPage = inventoryPage.logout();
        ExtentReportManager.logInfo("User logged out successfully");
    }

    @Then("I should be redirected to the login page")
    public void i_should_be_redirected_to_the_login_page() {
        loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should be redirected to login page");
        ExtentReportManager.logPass("Successfully redirected to login page");
    }

    @When("I proceed to checkout")
    public void i_proceed_to_checkout() {
        cartPage = new CartPage(driver);
        checkoutPage = cartPage.proceedToCheckout();
        ExtentReportManager.logInfo("Proceeded to checkout");
    }

    @When("I go back to inventory from cart")
    public void i_go_back_to_inventory_from_cart() {
        cartPage = new CartPage(driver);
        inventoryPage = cartPage.continueShopping();
        ExtentReportManager.logInfo("Returned to inventory from cart");
    }



    @When("I finish the checkout")
    public void i_finish_the_checkout() {
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
        checkoutCompletePage = checkoutOverviewPage.finishCheckout();
        ExtentReportManager.logInfo("Finished checkout process");
    }

    @Then("I should see the order confirmation page")
    public void i_should_see_the_order_confirmation_page() {
        checkoutCompletePage = new CheckoutCompletePage(driver);
        Assertions.assertTrue(checkoutCompletePage.isCheckoutCompletePageDisplayed(),
                "Should be on checkout complete page");
        ExtentReportManager.logPass("Order confirmation page displayed successfully");
    }

    @Then("I should see {string} message")
    public void i_should_see_message(String expectedMessage) {
        checkoutCompletePage = new CheckoutCompletePage(driver);
        String actualMessage = checkoutCompletePage.getConfirmationHeader();
        Assertions.assertEquals(expectedMessage, actualMessage,
                "Confirmation message should match expected");
        ExtentReportManager.logPass("Confirmation message verified: " + expectedMessage);
    }

    @When("I go back to products")
    public void i_go_back_to_products() {
        checkoutCompletePage = new CheckoutCompletePage(driver);
        inventoryPage = checkoutCompletePage.backToProducts();
        ExtentReportManager.logInfo("Returned to products page");
    }

    // Getter methods for page objects (to be used by other step classes)
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    public InventoryPage getInventoryPage() {
        if (inventoryPage == null) {
            inventoryPage = new InventoryPage(driver);
        }
        return inventoryPage;
    }

    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(driver);
        }
        return cartPage;
    }

    public CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) {
            checkoutPage = new CheckoutPage(driver);
        }
        return checkoutPage;
    }

    public CheckoutOverviewPage getCheckoutOverviewPage() {
        if (checkoutOverviewPage == null) {
            checkoutOverviewPage = new CheckoutOverviewPage(driver);
        }
        return checkoutOverviewPage;
    }
}