Feature: Login Feature
  Example of login cucumber feature

  Scenario: Login as a authenticated user
    Given user is  on homepage
    When user navigates to Login Page
    And user enters username and Password
    Then success message is displayed