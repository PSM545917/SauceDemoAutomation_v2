package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import java.util.List;

public class CartPage extends BasePage {

    // Elementos de la página del carrito
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "cart_quantity")
    private List<WebElement> itemQuantities;

    @FindBy(id = "remove-sauce-labs-backpack")
    private WebElement removeBackpackButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Métodos de verificación
    public boolean isCartPageDisplayed() {
        return isElementDisplayed(pageTitle) &&
                getText(pageTitle).equals("Your Cart");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getCartItemsCount() {
        return cartItems.size();
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    // Métodos de acción
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public void removeItemFromCart(String productName) {
        if (productName.equalsIgnoreCase("backpack")) {
            click(removeBackpackButton);
        }
    }

    // Métodos de obtención de información
    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .toList();
    }

    public List<String> getCartItemPrices() {
        return cartItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_price")).getText())
                .toList();
    }

    public List<String> getCartItemQuantities() {
        return itemQuantities.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.toLowerCase().contains(productName.toLowerCase()));
    }
}