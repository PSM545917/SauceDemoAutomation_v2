package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutCompletePage extends BasePage {

    // Elementos de la página de confirmación
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "complete-header")
    private WebElement confirmationHeader;

    @FindBy(className = "complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    @FindBy(className = "pony_express")
    private WebElement confirmationImage;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    // Métodos de verificación
    public boolean isCheckoutCompletePageDisplayed() {
        return isElementDisplayed(pageTitle) &&
                getText(pageTitle).equals("Checkout: Complete!");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public String getConfirmationHeader() {
        return getText(confirmationHeader);
    }

    public String getConfirmationText() {
        return getText(confirmationText);
    }

    public boolean isConfirmationImageDisplayed() {
        return isElementDisplayed(confirmationImage);
    }

    public boolean isOrderComplete() {
        return isCheckoutCompletePageDisplayed() &&
                getConfirmationHeader().equals("Thank you for your order!") &&
                isConfirmationImageDisplayed();
    }

    // Métodos de acción
    public InventoryPage backToProducts() {
        click(backToProductsButton);
        return new InventoryPage(driver);
    }
}