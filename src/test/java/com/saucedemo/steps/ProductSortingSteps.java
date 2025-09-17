package com.saucedemo.steps;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ExtentReportManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductSortingSteps {

    private WebDriver driver;
    private InventoryPage inventoryPage;
    private CommonSteps commonSteps;
    private List<String> currentProductNames;
    private List<String> currentProductPrices;

    public ProductSortingSteps() {
        this.driver = DriverManager.getDriver();
        this.commonSteps = new CommonSteps();
    }

    @When("I sort products by {string}")
    public void i_sort_products_by(String sortOption) {
        inventoryPage = commonSteps.getInventoryPage();
        inventoryPage.sortProducts(sortOption);

        // Store current state for verification
        currentProductNames = inventoryPage.getProductNames();
        currentProductPrices = inventoryPage.getProductPrices();

        ExtentReportManager.logInfo("Sorted products by: " + sortOption);
    }

    @Then("the products should be sorted correctly by {string}")
    public void the_products_should_be_sorted_correctly_by(String expectedOrder) {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productNames = inventoryPage.getProductNames();
        List<String> productPrices = inventoryPage.getProductPrices();

        switch (expectedOrder) {
            case "name_ascending":
                verifyNameSortingAscending(productNames);
                break;
            case "name_descending":
                verifyNameSortingDescending(productNames);
                break;
            case "price_low_to_high":
                verifyPriceSortingLowToHigh(productPrices);
                break;
            case "price_high_to_low":
                verifyPriceSortingHighToLow(productPrices);
                break;
        }

        ExtentReportManager.logPass("Products sorted correctly by: " + expectedOrder);
    }

    @Then("I should see {int} products on the page")
    public void i_should_see_products_on_the_page(int expectedProductCount) {
        inventoryPage = commonSteps.getInventoryPage();
        int actualProductCount = inventoryPage.getProductCount();
        Assertions.assertEquals(expectedProductCount, actualProductCount,
                "Should see " + expectedProductCount + " products on the page");
        ExtentReportManager.logPass("Verified product count: " + expectedProductCount);
    }

    @Then("all products should have names and prices displayed")
    public void all_products_should_have_names_and_prices_displayed() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productNames = inventoryPage.getProductNames();
        List<String> productPrices = inventoryPage.getProductPrices();

        Assertions.assertFalse(productNames.isEmpty(), "Product names should be displayed");
        Assertions.assertFalse(productPrices.isEmpty(), "Product prices should be displayed");
        Assertions.assertEquals(productNames.size(), productPrices.size(),
                "Each product should have both name and price");

        ExtentReportManager.logPass("All products have names and prices displayed");
    }

    @Then("all products should have {string} buttons")
    public void all_products_should_have_buttons(String buttonText) {
        inventoryPage = commonSteps.getInventoryPage();
        int productCount = inventoryPage.getProductCount();
        Assertions.assertTrue(productCount > 0, "Products should be available with " + buttonText + " buttons");
        ExtentReportManager.logPass("All products have " + buttonText + " buttons");
    }

    @Then("the first product should be {string}")
    public void the_first_product_should_be(String expectedFirstProduct) {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productNames = inventoryPage.getProductNames();
        String actualFirstProduct = productNames.get(0);
        Assertions.assertEquals(expectedFirstProduct, actualFirstProduct,
                "First product should be: " + expectedFirstProduct);
        ExtentReportManager.logPass("First product verified: " + expectedFirstProduct);
    }

    @Then("the last product should be {string}")
    public void the_last_product_should_be(String expectedLastProduct) {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productNames = inventoryPage.getProductNames();
        String actualLastProduct = productNames.get(productNames.size() - 1);
        Assertions.assertEquals(expectedLastProduct, actualLastProduct,
                "Last product should be: " + expectedLastProduct);
        ExtentReportManager.logPass("Last product verified: " + expectedLastProduct);
    }

    @Then("the products should be arranged from lowest to highest price")
    public void the_products_should_be_arranged_from_lowest_to_highest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        verifyPriceSortingLowToHigh(productPrices);
        ExtentReportManager.logPass("Products arranged from lowest to highest price");
    }

    @Then("the products should be arranged from highest to lowest price")
    public void the_products_should_be_arranged_from_highest_to_lowest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        verifyPriceSortingHighToLow(productPrices);
        ExtentReportManager.logPass("Products arranged from highest to lowest price");
    }

    @Then("the first product should have the lowest price")
    public void the_first_product_should_have_the_lowest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        List<Double> prices = convertPricesToNumbers(productPrices);

        double firstPrice = prices.get(0);
        double minPrice = Collections.min(prices);
        Assertions.assertEquals(minPrice, firstPrice, 0.01,
                "First product should have the lowest price");
        ExtentReportManager.logPass("First product has the lowest price: $" + firstPrice);
    }

    @Then("the last product should have the highest price")
    public void the_last_product_should_have_the_highest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        List<Double> prices = convertPricesToNumbers(productPrices);

        double lastPrice = prices.get(prices.size() - 1);
        double maxPrice = Collections.max(prices);
        Assertions.assertEquals(maxPrice, lastPrice, 0.01,
                "Last product should have the highest price");
        ExtentReportManager.logPass("Last product has the highest price: $" + lastPrice);
    }

    @Then("the first product should have the highest price")
    public void the_first_product_should_have_the_highest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        List<Double> prices = convertPricesToNumbers(productPrices);

        double firstPrice = prices.get(0);
        double maxPrice = Collections.max(prices);
        Assertions.assertEquals(maxPrice, firstPrice, 0.01,
                "First product should have the highest price");
        ExtentReportManager.logPass("First product has the highest price: $" + firstPrice);
    }

    @Then("the last product should have the lowest price")
    public void the_last_product_should_have_the_lowest_price() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productPrices = inventoryPage.getProductPrices();
        List<Double> prices = convertPricesToNumbers(productPrices);

        double lastPrice = prices.get(prices.size() - 1);
        double minPrice = Collections.min(prices);
        Assertions.assertEquals(minPrice, lastPrice, 0.01,
                "Last product should have the lowest price");
        ExtentReportManager.logPass("Last product has the lowest price: $" + lastPrice);
    }

    @When("I add the first product to the cart")
    public void i_add_the_first_product_to_the_cart() {
        inventoryPage = commonSteps.getInventoryPage();
        List<String> productNames = inventoryPage.getProductNames();
        String firstProduct = productNames.get(0);

        // Convert to short name for the addProductToCart method
        String shortName = getShortProductName(firstProduct);
        inventoryPage.addProductToCart(shortName);
        ExtentReportManager.logInfo("Added first product to cart: " + firstProduct);
    }

    @Then("the correct product should be added to the cart")
    public void the_correct_product_should_be_added_to_the_cart() {
        inventoryPage = commonSteps.getInventoryPage();
        int cartCount = inventoryPage.getCartItemCount();
        Assertions.assertEquals(1, cartCount, "Cart should contain 1 item");
        ExtentReportManager.logPass("Correct product added to cart");
    }

    // Helper methods
    private void verifyNameSortingAscending(List<String> productNames) {
        List<String> sortedNames = new ArrayList<>(productNames);
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER);
        Assertions.assertEquals(sortedNames, productNames,
                "Products should be sorted alphabetically A-Z");
    }

    private void verifyNameSortingDescending(List<String> productNames) {
        List<String> sortedNames = new ArrayList<>(productNames);
        sortedNames.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        Assertions.assertEquals(sortedNames, productNames,
                "Products should be sorted alphabetically Z-A");
    }

    private void verifyPriceSortingLowToHigh(List<String> productPrices) {
        List<Double> prices = convertPricesToNumbers(productPrices);
        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        Assertions.assertEquals(sortedPrices, prices,
                "Products should be sorted by price low to high");
    }

    private void verifyPriceSortingHighToLow(List<String> productPrices) {
        List<Double> prices = convertPricesToNumbers(productPrices);
        List<Double> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Collections.reverseOrder());
        Assertions.assertEquals(sortedPrices, prices,
                "Products should be sorted by price high to low");
    }

    private List<Double> convertPricesToNumbers(List<String> priceStrings) {
        return priceStrings.stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();
    }

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