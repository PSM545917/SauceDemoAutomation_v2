package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutOverviewPage extends BasePage {

    // Elementos de la página de resumen
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "cart_item")
    private java.util.List<WebElement> cartItems;

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    // Métodos de verificación
    public boolean isCheckoutOverviewPageDisplayed() {
        return isElementDisplayed(pageTitle) &&
                getText(pageTitle).equals("Checkout: Overview");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    // Métodos de obtención de información
    public String getSubtotal() {
        return getText(subtotalLabel);
    }

    public String getTax() {
        return getText(taxLabel);
    }

    public String getTotal() {
        return getText(totalLabel);
    }

    public int getOrderItemsCount() {
        return cartItems.size();
    }

    // Métodos de acción
    public CheckoutCompletePage finishCheckout() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public CartPage cancelCheckout() {
        click(cancelButton);
        return new CartPage(driver);
    }
}