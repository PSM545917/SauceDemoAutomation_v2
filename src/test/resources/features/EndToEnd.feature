@e2e
Feature: End-to-End Shopping Experience
  As a customer
  I want to complete a full shopping experience
  So that I can successfully purchase products from the store

  @smoke @critical
  Scenario: Complete shopping journey - Login to Purchase
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    Then I should be on the inventory page

    When I sort products by "lohi"
    And I add the following products to the cart:
      | product_name           |
      | Sauce Labs Onesie      |
      | Sauce Labs Backpack    |
    Then the cart should show 2 items

    When I go to the shopping cart
    Then I should see the following products in my cart:
      | product_name           | quantity |
      | Sauce Labs Onesie      | 1        |
      | Sauce Labs Backpack    | 1        |

    When I proceed to checkout
    And I fill the checkout information:
      | field       | value        |
      | firstName   | Test         |
      | lastName    | User         |
      | postalCode  | 12345        |
    And I continue to overview
    Then I should see the checkout overview page
    And I should see 2 items in the order summary

    When I finish the checkout
    Then I should see the order confirmation page
    And I should see "Thank you for your order!" message

    When I go back to products
    Then I should be on the inventory page
    And the cart should show 0 items

  @positive
  Scenario: Shopping with product removal
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    And I add the following products to the cart:
      | product_name            |
      | Sauce Labs Backpack     |
      | Sauce Labs Bike Light   |
      | Sauce Labs Bolt T-Shirt |
    Then the cart should show 3 items

    When I remove "Sauce Labs Bike Light" from the cart
    Then the cart should show 2 items

    When I go to the shopping cart
    Then I should see 2 products in the cart
    And I should not see "Sauce Labs Bike Light" in the cart

    When I remove "Sauce Labs Backpack" from the cart page
    Then the cart should contain 1 product

    When I continue shopping
    And I add "Sauce Labs Fleece Jacket" to the cart
    Then the cart should show 2 items

  @positive
  Scenario Outline: Complete purchase with different user data
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    And I add "Sauce Labs Backpack" to the cart
    And I go to the shopping cart
    And I proceed to checkout
    When I fill the checkout information:
      | field       | value         |
      | firstName   | <firstName>   |
      | lastName    | <lastName>    |
      | postalCode  | <postalCode>  |
    And I continue to overview
    And I finish the checkout
    Then I should see the order confirmation page
    And I should see "Thank you for your order!" message

    Examples:
      | firstName | lastName  | postalCode |
      | John      | Smith     | 10001      |
      | Maria     | Garcia    | 90210      |
      | David     | Johnson   | 73301      |
      | Sarah     | Wilson    | 60601      |

  @negative
  Scenario: Attempt checkout with empty cart
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    And I go to the shopping cart
    Then the cart should be empty
    And the checkout button should not be available for empty cart