Feature: Get System

  Scenario: A system is returned using the system code requesting as root
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The get system request is sent for code 'AFN'
    Then The response has an http status 200
    And The response contains system data
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |

  Scenario: A system is not found
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The get system request is sent for code 'NOT'
    Then The response has an http status 404

  Scenario: A denied response is returned if no authorisation header is provided
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    When The get system request is sent for code 'AFN' with no authorisation header
    Then The response has an http status 401

  Scenario: A denied response is returned if an empty jwt is provided
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    When The get system request is sent for code 'AFN' with an empty jwt
    Then The response has an http status 401

  Scenario: A forbidden response is returned if a not permitted role is provided
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'not-permitted'
    When The get system request is sent for code 'AFN'
    Then The response has an http status 403