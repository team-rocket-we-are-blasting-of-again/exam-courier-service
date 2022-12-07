Feature: Courier Registration
  It should be possible to add a Courier to the system.
  Courier should provide first name, last name, email.
  Created Courier should have user id and courier id
  As Courier is registered it should be saved with the provided data, courier id and user id

  Scenario: Register a Courier with uniq email
    Given a Courier with first name, last name, uniq email and uniq phone
    When Courier registers in the Service
    Then New Courier is created with first name last name, email, phone
    And New Courier has courier id and user id

  Scenario:  Register a Courier with existing email
    Given a Courier with existing email
    When Courier registers in the Service with invalid email
    Then ResourceException is thrown

  Scenario:  Register a Courier with existing phone
    Given a Courier with existing phone
    When Courier registers in the Service with invalid phone
    Then ResourceException is thrown

