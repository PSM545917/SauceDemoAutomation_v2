@login
Feature: Login functionality
  As a user
  I want to login to the SauceDemo application
  So that I can access the inventory page

  Background:
    Given I am on the SauceDemo login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When I enter username "standard_user" and password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And I should see "Products" as the page title

  @negative
  Scenario: Login with invalid username
    When I enter username "invalid_user" and password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative
  Scenario: Login with invalid password
    When I enter username "standard_user" and password "wrong_password"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative
  Scenario Outline: Login with different invalid credentials
    When I enter username "<username>" and password "<password>"
    And I click the login button
    Then I should see an error message "<error_message>"
    And I should remain on the login page

    Examples:
      | username      | password      | error_message                                                                 |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                         |
      |               | secret_sauce  | Epic sadface: Username is required                                          |
      | standard_user |               | Epic sadface: Password is required                                          |

  @positive
  Scenario: Logout functionality
    When I login with username "standard_user" and password "secret_sauce"
    Then I should be on the inventory page
    When I logout from the application
    Then I should be redirected to the login page