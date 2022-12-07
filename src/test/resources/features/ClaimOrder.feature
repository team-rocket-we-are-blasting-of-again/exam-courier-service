Feature: Courier Claims Order to deliver
  Each time a restaurant accepts an order,
  courier service should show new delivery task to a courier assigned to the specific area
  where the order should be picked up from.
  Courier then should be able to claim that delivery.

  Background:
    Given a delivery task received from restaurant with id 1 in area "cph"
    And a courier assigned to area "cph"

    Scenario:


