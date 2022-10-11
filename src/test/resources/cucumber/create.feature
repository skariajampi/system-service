Feature: Create System

  Scenario: A system is created
    Given A create system request is created with
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The create system request is sent
    Then The response has an http status 201
    And The response contains system data
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The database only contains records
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |

  Scenario: A system with the same code already exists in the database
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And A create system request is created with
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The create system request is sent
    Then The response has an http status 400

  Scenario Outline: A system request fails validation
    Given A create system request is created with
      | code   | name   | description   |
      | <code> | <name> | <description> |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The create system request is sent
    Then The response has an http status 400
    Examples:
      | code    | name    | description |
      |         | name    | description |
      | [blank] | name    | description |
      | code    |         | description |
      | code    | [blank] | description |
      | code    | name    |             |
      | code    | name    | [blank]     |

  Scenario: A denied response is returned if no authorisation header is provided
    Given A create system request is created with
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    When The create system request is sent with no authorisation header
    Then The response has an http status 401

  Scenario: A forbidden response is returned if a not permitted role is provided
    Given A create system request is created with
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'not-permitted'
    When The create system request is sent
    Then The response has an http status 403