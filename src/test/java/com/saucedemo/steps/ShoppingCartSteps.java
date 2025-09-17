package com.saucedemo.steps;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.CartPage;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ExtentReportManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class ShoppingCartSteps {

    private WebDriver driver;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CommonSteps commonSteps;

    public ShoppingCartSteps() {
        this.driver = DriverManager.getDriver();
        this.commonSteps = new CommonSteps();
    }

    @When("I add {string} to the cart")
    public void i_add_to_the_cart(String productName) {
        inventoryPage = commonSteps.getInventoryPage();
        String shortName = getShortProductName(productName);
        inventoryPage.addProductToCart(shortName);
        ExtentReportManager.logInfo("Added product to cart: " + productName);
    }

    @When("I add the following products to the cart:")
    public void i_add_the_following_products_to_the_cart(DataTable dataTable) {
        inventoryPage = commonSteps.getInventoryPage();
        List<Map<String, String>> products = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> product : products) {
            String productName = product.get("product_name");
            String shortName = getShortProductName(productName);
            inventoryPage.addProductToCart(shortName);
            ExtentReportManager.logInfo("Added product to cart: " + productName);
        }
    }

    @Given("I have added {string} to the cart")
    public void i_have_added_to_the_cart(String productName) {
        i_add_to_the_cart(productName);
    }

    @When("I remove {string} from the cart")
    public void i_remove_from_the_cart(String productName) {
        inventoryPage = commonSteps.getInventoryPage();
        String shortName = getShortProductName(productName);
        inventoryPage.removeProductFromCart(shortName);
        ExtentReportManager.logInfo("Removed product from cart: " + productName);
    }

    @When("I remove {string} from the cart page")
    public void i_remove_from_the_cart_page(String productName) {
        cartPage = commonSteps.getCartPage();
        String shortName = getShortProductName(productName);
        cartPage.removeItemFromCart(shortName);
        ExtentReportManager.logInfo("Removed product from cart page: " + productName);
    }

    @When("I click continue shopping")
    public void i_click_continue_shopping() {
        cartPage = commonSteps.getCartPage();
        inventoryPage = cartPage.continueShopping();
        ExtentReportManager.logInfo("Clicked continue shopping");
    }

    @Then("the cart should show {int} item(s)")
    public void the_cart_should_show_items(int expectedCount) {
        inventoryPage = commonSteps.getInventoryPage();
        int actualCount = inventoryPage.getCartItemCount();
        Assertions.assertEquals(expectedCount, actualCount,
                "Cart should show " + expectedCount + " items");
        ExtentReportManager.logPass("Cart shows correct item count: " + expectedCount);
    }

    @Then("I should see {string} in the cart")
    public void i_should_see_in_the_cart(String productName) {
        cartPage = commonSteps.getCartPage();
        Assertions.assertTrue(cartPage.isProductInCart(productName),
                "Product should be in cart: " + productName);
        ExtentReportManager.logPass("Product found in cart: " + productName);
    }

    @Then("I should not see {string} in the cart")
    public void i_should_not_see_in_the_cart(String productName) {
        cartPage = commonSteps.getCartPage();
        Assertions.assertFalse(cartPage.isProductInCart(productName),
                "Product should not be in cart: " + productName);
        ExtentReportManager.logPass("Product not found in cart as expected: " + productName);
    }

    @Then("the cart should contain {int} product(s)")
    public void the_cart_should_contain_products(int expectedCount) {
        cartPage = commonSteps.getCartPage();
        int actualCount = cartPage.getCartItemsCount();
        Assertions.assertEquals(expectedCount, actualCount,
                "Cart should contain " + expectedCount + " products");
        ExtentReportManager.logPass("Cart contains correct number of products: " + expectedCount);
    }

    @Then("the cart should be empty")
    public void the_cart_should_be_empty() {
        cartPage = commonSteps.getCartPage();
        Assertions.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        ExtentReportManager.logPass("Cart is empty as expected");
    }

    @Then("I should see no products in the cart")
    public void i_should_see_no_products_in_the_cart() {
        the_cart_should_be_empty();
    }

    @Then("the cart should contain the following products:")
    public void the_cart_should_contain_the_following_products(DataTable dataTable) {
        cartPage = commonSteps.getCartPage();
        List<Map<String, String>> expectedProducts = dataTable.asMaps(String.class, String.class);
        List<String> actualProducts = cartPage.getCartItemNames();

        for (Map<String, String> expectedProduct : expectedProducts) {
            String productName = expectedProduct.get("product_name");
            Assertions.assertTrue(actualProducts.stream()
                            .anyMatch(actual -> actual.contains(productName.replace("Sauce Labs ", ""))),
                    "Cart should contain product: " + productName);
        }

        ExtentReportManager.logPass("All expected products found in cart");
    }

    @Then("I should see the following products in my cart:")
    public void i_should_see_the_following_products_in_my_cart(DataTable dataTable) {
        cartPage = commonSteps.getCartPage();
        List<Map<String, String>> expectedProducts = dataTable.asMaps(String.class, String.class);
        List<String> actualProducts = cartPage.getCartItemNames();
        List<String> actualQuantities = cartPage.getCartItemQuantities();

        Assertions.assertEquals(expectedProducts.size(), actualProducts.size(),
                "Number of products in cart should match expected");

        for (int i = 0; i < expectedProducts.size(); i++) {
            Map<String, String> expectedProduct = expectedProducts.get(i);
            String expectedName = expectedProduct.get("product_name");
            String expectedQuantity = expectedProduct.get("quantity");

            Assertions.assertTrue(actualProducts.get(i).contains(expectedName.replace("Sauce Labs ", "")),
                    "Product name should match: " + expectedName);
            Assertions.assertEquals(expectedQuantity, actualQuantities.get(i),
                    "Product quantity should match: " + expectedQuantity);
        }

        ExtentReportManager.logPass("All products and quantities verified in cart");
    }

    @Then("the cart should still show {int} item(s)")
    public void the_cart_should_still_show_items(int expectedCount) {
        the_cart_should_show_items(expectedCount);
    }

    @Then("the checkout button should not be available for empty cart")
    public void the_checkout_button_should_not_be_available_for_empty_cart() {
        cartPage = commonSteps.getCartPage();
        Assertions.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        ExtentReportManager.logInfo("Verified checkout button availability for empty cart");
    }

    // Utility method to convert full product names to short names used in page methods
    private String getShortProductName(String fullProductName) {
        switch (fullProductName) {
            case "Sauce Labs Backpack":
                return "backpack";
            case "Sauce Labs Bike Light":
                return "bike light";
            case "Sauce Labs Bolt T-Shirt":
                return "t-shirt";
            case "Sauce Labs Fleece Jacket":
                return "fleece jacket";
            case "Sauce Labs Onesie":
                return "onesie";
            case "Test.allTheThings() T-Shirt (Red)":
                return "red t-shirt";
            default:
                return fullProductName.toLowerCase();
        }
    }
}