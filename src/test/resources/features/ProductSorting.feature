@sorting
Feature: Product Sorting and Filtering
  As a user on the inventory page
  I want to sort products by different criteria
  So that I can find products more easily

  Background:
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    Then I should be on the inventory page

  @positive
  Scenario Outline: Sort products by different criteria
    When I sort products by "<sort_option>"
    Then the products should be sorted correctly by "<expected_order>"

    Examples:
      | sort_option | expected_order        |
      | az          | name_ascending        |
      | za          | name_descending       |
      | lohi        | price_low_to_high     |
      | hilo        | price_high_to_low     |

  @smoke @positive
  Scenario: Verify default product display
    Then I should see 6 products on the page
    And all products should have names and prices displayed
    And all products should have "Add to cart" buttons

  @positive
  Scenario: Sort by name A to Z
    When I sort products by "az"
    Then the first product should be "Sauce Labs Backpack"
    And the last product should be "Test.allTheThings() T-Shirt (Red)"

  @positive
  Scenario: Sort by name Z to A
    When I sort products by "za"
    Then the first product should be "Test.allTheThings() T-Shirt (Red)"
    And the last product should be "Sauce Labs Backpack"

  @positive
  Scenario: Sort by price low to high
    When I sort products by "lohi"
    Then the products should be arranged from lowest to highest price
    And the first product should have the lowest price
    And the last product should have the highest price

  @positive
  Scenario: Sort by price high to low
    When I sort products by "hilo"
    Then the products should be arranged from highest to lowest price
    And the first product should have the highest price
    And the last product should have the lowest price

  @positive
  Scenario: Add product to cart after sorting
    When I sort products by "hilo"
    And I add the first product to the cart
    Then the cart should show 1 item
    And the correct product should be added to the cart