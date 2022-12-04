Feature: Courier Registration
  It should be possible to add a Courier to the system.
  Courier should provide first name, last name, email.
  Created Courier should have user id and courier id
  When Courier is registered it should be saved with the provided data, courier id and user id
  Scenario: Register a Courier with uniq email
    Given a Courier with first name "Magdalena", last name "Wawrzak" and uniq email "magda@mail.com"
    When Courier registers in the Service
    Then New Courier is created with with first name "Magdalena", last name "Wawrzak", email "magda@mail.com"
    And New Courier has courier id and user id