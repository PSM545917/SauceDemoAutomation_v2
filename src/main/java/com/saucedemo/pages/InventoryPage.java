package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import java.util.List;

public class InventoryPage extends BasePage {

    // Elementos de la página
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    // Botones de productos específicos
    @FindBy(id = "add-to-cart-sauce-labs-backpack")
    private WebElement addBackpackButton;

    @FindBy(id = "add-to-cart-sauce-labs-bike-light")
    private WebElement addBikeLightButton;

    @FindBy(id = "add-to-cart-sauce-labs-bolt-t-shirt")
    private WebElement addTShirtButton;

    @FindBy(id = "remove-sauce-labs-backpack")
    private WebElement removeBackpackButton;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // Métodos de verificación
    public boolean isInventoryPageDisplayed() {
        return isElementDisplayed(pageTitle) &&
                getText(pageTitle).equals("Products");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getCartItemCount() {
        if (isElementDisplayed(cartBadge)) {
            return Integer.parseInt(getText(cartBadge));
        }
        return 0;
    }

    // Métodos de acción
    public void addProductToCart(String productName) {
        switch (productName.toLowerCase()) {
            case "backpack":
                click(addBackpackButton);
                break;
            case "bike light":
                click(addBikeLightButton);
                break;
            case "t-shirt":
                click(addTShirtButton);
                break;
        }
    }

    public void removeProductFromCart(String productName) {
        switch (productName.toLowerCase()) {
            case "backpack":
                click(removeBackpackButton);
                break;
        }
    }

    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }

    public void openMenu() {
        click(menuButton);
    }

    public LoginPage logout() {
        openMenu();
        click(logoutLink);
        return new LoginPage(driver);
    }

    public void sortProducts(String sortOption) {
        click(sortDropdown);
        WebElement option = driver.findElement(
                By.xpath("//option[@value='" + sortOption + "']")
        );
        click(option);
    }

    public int getProductCount() {
        return inventoryItems.size();
    }

    public List<String> getProductNames() {
        return inventoryItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .toList();
    }

    public List<String> getProductPrices() {
        return inventoryItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_price")).getText())
                .toList();
    }
}