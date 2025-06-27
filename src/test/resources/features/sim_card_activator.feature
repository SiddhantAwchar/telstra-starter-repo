Feature: SIM card activation

  Scenario: Successful SIM card activation
    Given I have a SIM card with ICCID "1255789453849037777" and customer email "success@example.com"
    When I activate the SIM card
    Then the activation result for ID 1 should be:
      | iccid          | customerEmail      | active |
      | 1255789453849037777 | success@example.com | true   |

  Scenario: Failed SIM card activation
    Given I have a SIM card with ICCID "8944500102198304826" and customer email "fail@example.com"
    When I activate the SIM card
    Then the activation result for ID 2 should be:
      | iccid          | customerEmail     | active |
      | 8944500102198304826 | fail@example.com | false  |
