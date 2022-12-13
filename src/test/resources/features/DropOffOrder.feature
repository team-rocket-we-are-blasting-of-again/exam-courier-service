@cleanUpDb
Feature: Courier Drops off picked order.
  After A courier had claimed an order and picked it up from a restaurant,
  courier can drop order off when reached the order destination
  and the system gets notified.

  Scenario: Drop off picked up order
    Given A courier and a delivery task that has been picked up by that courier
    When Courier drops of the order of that task
    Then Delivery status is sat to COMPLETED and system is notified