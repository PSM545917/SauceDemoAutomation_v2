package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    // Elementos del formulario de checkout
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement errorMessage;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Métodos de verificación
    public boolean isCheckoutPageDisplayed() {
        return isElementDisplayed(pageTitle) &&
                getText(pageTitle).equals("Checkout: Your Information");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    // Métodos de acción
    public void enterFirstName(String firstName) {
        sendKeys(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        sendKeys(lastNameField, lastName);
    }

    public void enterPostalCode(String postalCode) {
        sendKeys(postalCodeField, postalCode);
    }

    public CheckoutOverviewPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    public void fillCheckoutInformationWithError(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        click(continueButton);
    }

    public CartPage cancelCheckout() {
        click(cancelButton);
        return new CartPage(driver);
    }

    public void clearAllFields() {
        firstNameField.clear();
        lastNameField.clear();
        postalCodeField.clear();
    }
}