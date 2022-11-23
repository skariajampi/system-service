Feature: Delete System

  Scenario: A system is deleted
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | AFN  | Apply for Nino       | Apply for Nino Description       |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    Given The client is authenticated with roles 'VISITOR'
    When The delete system request is sent for code 'AFN'
    Then The response has an http status 204
    And The database only contains records
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |

  Scenario: Attempt to delete a system with a code that does not exist in the database
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    And The client is authenticated with roles 'VISITOR'
    When The delete system request is sent for code 'AFN'
    Then The response has an http status 404
    And The database only contains records
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |

  Scenario: A denied response is returned if no authorisation header is provided
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    When The delete system request is sent for code 'AFN' with no authorisation header
    Then The response has an http status 401
    And The database only contains records
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |

  Scenario: A forbidden response is returned if a not permitted role is provided
    Given System database records exist
      | code | name           | description                |
      | AFN  | Apply for Nino | Apply for Nino Description |
    And The client is authenticated with roles 'not-permitted'
    When The delete system request is sent for code 'AFN'
    Then The response has an http status 403