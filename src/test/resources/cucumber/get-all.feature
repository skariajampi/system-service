Feature: Get All Systems

  Scenario: All systems are returned by requesting as root
    Given System database records exist
      | id                                   | code | name                 | description                      |
      | eb8a3639-4d90-4712-ab8b-f49efb6cdd44 | AFN  | Apply for Nino       | Apply for Nino Description       |
      | 8A7089E8-3EA5-4CCB-9272-D0E94986F369 | JSA  | Job Seeker Allowance | Job Seeker Allowance Description |

    And The client is authenticated with roles 'MyGreeterLambda'
    When The getAll systems request is sent
    Then The response has an http status 200
    And The getAll systems response contains
      | id                                   | code | name                 | description                      |
      | eb8a3639-4d90-4712-ab8b-f49efb6cdd44 | AFN  | Apply for Nino       | Apply for Nino Description       |
      | 8A7089E8-3EA5-4CCB-9272-D0E94986F369 | JSA  | Job Seeker Allowance | Job Seeker Allowance Description |

  Scenario: A denied response is returned if no authorisation header is provided
    Given System database records exist
      | id                                   | code | name                 | description                      |
      | eb8a3639-4d90-4712-ab8b-f49efb6cdd44 | AFN  | Apply for Nino       | Apply for Nino Description       |
      | 8A7089E8-3EA5-4CCB-9272-D0E94986F369 | JSA  | Job Seeker Allowance | Job Seeker Allowance Description |

    When The getAll systems request is sent with no authorisation header
    Then The response has an http status 401


  Scenario: A denied response is returned if an empty jwt is provided
    Given System database records exist
      | id                                   | code | name                 | description                      |
      | eb8a3639-4d90-4712-ab8b-f49efb6cdd44 | AFN  | Apply for Nino       | Apply for Nino Description       |
      | 8A7089E8-3EA5-4CCB-9272-D0E94986F369 | JSA  | Job Seeker Allowance | Job Seeker Allowance Description |

    When The getAll systems request is sent with an empty jwt
    Then The response has an http status 401


  Scenario: A forbidden response is returned if a not permitted role is provided
    Given System database records exist
      | id                                   | code | name                 | description                      |
      | eb8a3639-4d90-4712-ab8b-f49efb6cdd44 | AFN  | Apply for Nino       | Apply for Nino Description       |
      | 8A7089E8-3EA5-4CCB-9272-D0E94986F369 | JSA  | Job Seeker Allowance | Job Seeker Allowance Description |
    And The client is authenticated with roles 'not-permitted'
    When The getAll systems request is sent
    Then The response has an http status 403

