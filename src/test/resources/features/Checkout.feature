@checkout
Feature: Checkout Process
  As a user with items in my cart
  I want to complete the checkout process
  So that I can purchase my selected products

  Background:
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    And I add "Sauce Labs Backpack" to the cart
    And I go to the shopping cart
    And I proceed to checkout

  @smoke @positive @e2e
  Scenario: Complete checkout with valid information
    When I fill the checkout information:
      | field       | value      |
      | firstName   | John       |
      | lastName    | Doe        |
      | postalCode  | 12345      |
    And I continue to overview
    Then I should see the checkout overview page
    And I should see the order summary with correct items
    When I finish the checkout
    Then I should see the order confirmation page
    And I should see "Thank you for your order!" message

  @negative
  Scenario Outline: Checkout with missing required fields
    When I fill the checkout information with missing "<missing_field>":
      | field       | value      |
      | firstName   | <firstName> |
      | lastName    | <lastName>  |
      | postalCode  | <postalCode> |
    And I continue to overview
    Then I should see error message "<error_message>"
    And I should remain on the checkout information page

    Examples:
      | missing_field | firstName | lastName | postalCode | error_message                    |
      | firstName     |           | Doe      | 12345      | Error: First Name is required    |
      | lastName      | John      |          | 12345      | Error: Last Name is required     |
      | postalCode    | John      | Doe      |            | Error: Postal Code is required   |

  @positive
  Scenario: Cancel checkout from information page
    When I cancel the checkout from information page
    Then I should be redirected to the cart page
    And I should still see my products in the cart

  @positive
  Scenario: Cancel checkout from overview page
    When I fill the checkout information:
      | field       | value      |
      | firstName   | John       |
      | lastName    | Doe        |
      | postalCode  | 12345      |
    And I continue to overview
    When I cancel the checkout from overview page
    Then I should be redirected to the cart page
    And I should still see my products in the cart

  @positive @e2e
  Scenario: Complete checkout with multiple products
    Given I go back to inventory from cart
    And I add "Sauce Labs Bike Light" to the cart
    And I go to the shopping cart
    And I proceed to checkout
    When I fill the checkout information:
      | field       | value      |
      | firstName   | Jane       |
      | lastName    | Smith      |
      | postalCode  | 67890      |
    And I continue to overview
    Then I should see 2 items in the order summary
    When I finish the checkout
    Then I should see the order confirmation page