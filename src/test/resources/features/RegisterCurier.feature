Feature: Courier Registration
  It should be possible to add a Courier to the system.
  Courier should provide first name, last name, email.
  Created Courier should have user id and courier id
  As Courier is registered it should be saved with the provided data, courier id and user id

  Scenario: Register a Courier with uniq email
    Given a Courier with first name "Magdalena", last name "Wawrzak", uniq email "magda@mail.com" and uniq phone "123"
    When Courier registers in the Service
    Then New Courier is created with first name "Magdalena", last name "Wawrzak", email "magda@mail.com", phone "123"
    And New Courier has courier id and user id

  Scenario Outline: Register a Courier with existing email or  phone
    Given a Courier with first name "Magdalena", last name "Wawrzak", existing email <email> or existing phone <phone>
    When Courier registers in the Service
    Then ResourceException is thrown
    Examples:
      | email | phone |
      | "existing@mail" | "NOTexistingphone" |
      | "notexisting@mail" | "existingphone" |

#
#  Scenario: Send Kafka Event when new Customer is created
#    Given valid Courier
#    When Courier registers in the Service
#    Then Kafka event is emitted
#
