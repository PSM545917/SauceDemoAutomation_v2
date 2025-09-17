package com.saucedemo.steps;

import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.CartPage;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ExtentReportManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class CheckoutSteps {

    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CartPage cartPage;
    private CommonSteps commonSteps;

    public CheckoutSteps() {
        this.driver = DriverManager.getDriver();
        this.commonSteps = new CommonSteps();
    }

    @When("I fill the checkout information:")
    public void i_fill_the_checkout_information(DataTable dataTable) {
        checkoutPage = commonSteps.getCheckoutPage();
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> checkoutData = data.get(0);

        String firstName = checkoutData.get("firstName");
        String lastName = checkoutData.get("lastName");
        String postalCode = checkoutData.get("postalCode");

        checkoutOverviewPage = checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        ExtentReportManager.logInfo("Filled checkout information - Name: " + firstName + " " + lastName + ", ZIP: " + postalCode);
    }

    @When("I fill the checkout information with missing {string}:")
    public void i_fill_the_checkout_information_with_missing(String missingField, DataTable dataTable) {
        checkoutPage = commonSteps.getCheckoutPage();
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> checkoutData = data.get(0);

        String firstName = checkoutData.get("firstName");
        String lastName = checkoutData.get("lastName");
        String postalCode = checkoutData.get("postalCode");

        // Fill information with potential missing fields
        checkoutPage.fillCheckoutInformationWithError(firstName, lastName, postalCode);
        ExtentReportManager.logInfo("Filled checkout information with missing: " + missingField);
    }

    @When("I continue to overview")
    public void i_continue_to_overview() {
        // This step is handled by the fillCheckoutInformation method
        // Just verify we're on the overview page if successful
        if (!commonSteps.getCheckoutPage().isErrorMessageDisplayed()) {
            checkoutOverviewPage = commonSteps.getCheckoutOverviewPage();
        }
        ExtentReportManager.logInfo("Attempted to continue to overview");
    }

    @When("I cancel the checkout from information page")
    public void i_cancel_the_checkout_from_information_page() {
        checkoutPage = commonSteps.getCheckoutPage();
        cartPage = checkoutPage.cancelCheckout();
        ExtentReportManager.logInfo("Cancelled checkout from information page");
    }

    @When("I cancel the checkout from overview page")
    public void i_cancel_the_checkout_from_overview_page() {
        checkoutOverviewPage = commonSteps.getCheckoutOverviewPage();
        cartPage = checkoutOverviewPage.cancelCheckout();
        ExtentReportManager.logInfo("Cancelled checkout from overview page");
    }

    @Then("I should see the checkout overview page")
    public void i_should_see_the_checkout_overview_page() {
        checkoutOverviewPage = commonSteps.getCheckoutOverviewPage();
        Assertions.assertTrue(checkoutOverviewPage.isCheckoutOverviewPageDisplayed(),
                "Should be on checkout overview page");
        ExtentReportManager.logPass("Checkout overview page displayed successfully");
    }

    @Then("I should see the order summary with correct items")
    public void i_should_see_the_order_summary_with_correct_items() {
        checkoutOverviewPage = commonSteps.getCheckoutOverviewPage();
        int itemCount = checkoutOverviewPage.getOrderItemsCount();
        Assertions.assertTrue(itemCount > 0, "Order summary should show items");

        String subtotal = checkoutOverviewPage.getSubtotal();
        String tax = checkoutOverviewPage.getTax();
        String total = checkoutOverviewPage.getTotal();

        Assertions.assertNotNull(subtotal, "Subtotal should be displayed");
        Assertions.assertNotNull(tax, "Tax should be displayed");
        Assertions.assertNotNull(total, "Total should be displayed");

        ExtentReportManager.logPass("Order summary verified with " + itemCount + " items");
    }

    @Then("I should see error message {string}")
    public void i_should_see_error_message(String expectedErrorMessage) {
        checkoutPage = commonSteps.getCheckoutPage();
        Assertions.assertTrue(checkoutPage.isErrorMessageDisplayed(),
                "Error message should be displayed");

        String actualErrorMessage = checkoutPage.getErrorMessage();
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage,
                "Error message should match expected value");
        ExtentReportManager.logPass("Error message verified: " + expectedErrorMessage);
    }

    @Then("I should remain on the checkout information page")
    public void i_should_remain_on_the_checkout_information_page() {
        checkoutPage = commonSteps.getCheckoutPage();
        Assertions.assertTrue(checkoutPage.isCheckoutPageDisplayed(),
                "Should remain on checkout information page");
        ExtentReportManager.logPass("Remained on checkout information page as expected");
    }

    @Then("I should be redirected to the cart page")
    public void i_should_be_redirected_to_the_cart_page() {
        cartPage = commonSteps.getCartPage();
        Assertions.assertTrue(cartPage.isCartPageDisplayed(),
                "Should be redirected to cart page");
        ExtentReportManager.logPass("Successfully redirected to cart page");
    }

    @Then("I should still see my products in the cart")
    public void i_should_still_see_my_products_in_the_cart() {
        cartPage = commonSteps.getCartPage();
        int itemCount = cartPage.getCartItemsCount();
        Assertions.assertTrue(itemCount > 0, "Cart should still contain products");
        ExtentReportManager.logPass("Products still present in cart: " + itemCount + " items");
    }

    @Then("I should see {int} items in the order summary")
    public void i_should_see_items_in_the_order_summary(int expectedItemCount) {
        checkoutOverviewPage = commonSteps.getCheckoutOverviewPage();
        int actualItemCount = checkoutOverviewPage.getOrderItemsCount();
        Assertions.assertEquals(expectedItemCount, actualItemCount,
                "Order summary should show " + expectedItemCount + " items");
        ExtentReportManager.logPass("Order summary shows correct item count: " + expectedItemCount);
    }
}