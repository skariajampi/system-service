Feature: Management Endpoints

  Scenario: The spring doc endpoint is not secured
    When A request is sent for endpoint "/contract"
    Then The response has an http status 200

  Scenario: The health endpoint is not secured
    When A request is sent for endpoint "/actuator/health"
    Then The response has an http status 200

  Scenario: The metrics endpoint is not secured
    When A request is sent for endpoint "/actuator/metrics"
    Then The response has an http status 200

  Scenario: The info endpoint is not secured
    When A request is sent for endpoint "/actuator/info"
    Then The response has an http status 200
