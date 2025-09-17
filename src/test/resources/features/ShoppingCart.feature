@shoppingcart
Feature: Shopping Cart functionality
  As a logged-in user
  I want to add and manage products in my shopping cart
  So that I can purchase the items I need

  Background:
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    Then I should be on the inventory page

  @smoke @positive
  Scenario: Add single product to cart
    When I add "Sauce Labs Backpack" to the cart
    Then the cart should show 1 item
    When I go to the shopping cart
    Then I should see "Sauce Labs Backpack" in the cart
    And the cart should contain 1 product

  @positive
  Scenario: Add multiple products to cart
    When I add the following products to the cart:
      | product_name           |
      | Sauce Labs Backpack    |
      | Sauce Labs Bike Light  |
      | Sauce Labs Bolt T-Shirt |
    Then the cart should show 3 items
    When I go to the shopping cart
    Then the cart should contain the following products:
      | product_name           |
      | Sauce Labs Backpack    |
      | Sauce Labs Bike Light  |
      | Sauce Labs Bolt T-Shirt |

  @positive
  Scenario: Remove product from cart in inventory page
    Given I have added "Sauce Labs Backpack" to the cart
    When I remove "Sauce Labs Backpack" from the cart
    Then the cart should show 0 items
    When I go to the shopping cart
    Then the cart should be empty

  @positive
  Scenario: Remove product from cart in cart page
    Given I have added "Sauce Labs Backpack" to the cart
    When I go to the shopping cart
    And I remove "Sauce Labs Backpack" from the cart page
    Then the cart should be empty
    And I should see no products in the cart

  @positive
  Scenario: Continue shopping from cart page
    Given I have added "Sauce Labs Backpack" to the cart
    When I go to the shopping cart
    And I click continue shopping
    Then I should be on the inventory page
    And the cart should still show 1 item