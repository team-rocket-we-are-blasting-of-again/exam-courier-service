Feature: Courier Registration
  It should be possible to add a Courier to the system.
  Courier should provide first name, last name, email.
  Created Courier should have user id and courier id
  As Courier is registered it should be saved with the provided data, courier id and user id

  Scenario: Register a Courier with uniq email
    Given a Courier with first name "Magdalena", last name "Wawrzak" and uniq email "magda@mail.com"
    When Courier registers in the Service
    Then New Courier is created with first name "Magdalena", last name "Wawrzak", email "magda@mail.com"
    And New Courier has courier id and user id

  Scenario: Register a Courier with existing email
    Given a Courier with first name "Magdalena", last name "Wawrzak" and existing email "existing@mail.com"
    When Courier registers in the Service
    Then ResourceException is thrown

  Scenario: Send Kafka Event when new Customer is created
    Given valid Courier
    When Courier registers in the Service
    Then Kafka event is emitted

