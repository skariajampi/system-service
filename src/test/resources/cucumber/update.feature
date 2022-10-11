Feature: Update System

  Scenario: A system is updated
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | AFN  | Apply for Nino       | Apply for Nino Description       |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    Given The client is authenticated with roles 'MyGreeterLambda'
    And An update system request is created with
      | code | name                 | description                      |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
    When The update system request is sent for code 'AFN'
    Then The response has an http status 200
    And The response contains system data
      | code | name                 | description                      |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
    And The database only contains records
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |

  Scenario: Attempt to update a system with a code that already exists in the database
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | AFN  | Apply for Nino       | Apply for Nino Description       |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    Given The client is authenticated with roles 'MyGreeterLambda'
    And An update system request is created with
      | code | name                 | description                      |
      | MA   | Diff Apply for Nino! | Diff Apply for Nino Description! |
    When The update system request is sent for code 'AFN'
    Then The response has an http status 400
    And The database only contains records
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | AFN  | Apply for Nino       | Apply for Nino Description       |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |

  Scenario: Attempt to update a system with a code that does not exist in the database
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    And An update system request is created with
      | code | name                 | description                      |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
    And The client is authenticated with roles 'MyGreeterLambda'
    When The update system request is sent for code 'AFN'
    Then The response has an http status 404
    And The database only contains records
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |

  Scenario: A denied response is returned if no authorisation header is provided
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    And An update system request is created with
      | code | name                 | description                      |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
    When The update system request is sent for code 'AFN' with no authorisation header
    Then The response has an http status 401


  Scenario: A forbidden response is returned if a not permitted role is provided
    Given System database records exist
      | code | name                 | description                      |
      | MA   | Maternity Allowance  | Maternity Allowance Description  |
      | JSA  | Jobseekers Allowance | Jobseekers Allowance Description |
    And An update system request is created with
      | code | name                 | description                      |
      | AFN  | Diff Apply for Nino! | Diff Apply for Nino Description! |
    And The client is authenticated with roles 'not-permitted'
    When The update system request is sent for code 'AFN'
    Then The response has an http status 403
