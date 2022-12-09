@cleanUpDb
Feature: Courier Claims Order to deliver
  Each time a restaurant accepts an order,
  courier service should show new delivery task to a courier assigned to the specific area
  where the order should be picked up from.
  Courier then should be able to claim that delivery.

  Background:
    Given a delivery received from restaurant with area "cph"
    And a courier that is online and assigned to area "cph"

    Scenario:
      When courier claims a task
      Then Response status is OK
      And Delivery is assigned to courier and has status ON_THE_WAY





