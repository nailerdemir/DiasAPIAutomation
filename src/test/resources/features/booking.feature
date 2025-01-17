Feature: Hotel Reservation System

  Scenario: Create a new authentication token
    Given the user connects to the API
    When the user sends a POST request to '/auth' with valid credentials
    Then the response status code should be 200
    And the response should contain a token

  Scenario: Retrieve all booking IDs
    Given the user connects to the API
    When the user sends a GET request to '/booking'
    Then the response status code should be 200
    And the response should contain a list of booking IDs

  Scenario: Retrieve booking details by ID
    Given the user connects to the API
    And a booking with ID '1' exists
    When the user sends a GET request to '/booking/1'
    Then the response status code should be 200
    And the response should contain the booking details

  Scenario: Create a new booking
    Given the user connects to the API
    When the user sends a POST request to '/booking' with valid booking data
    Then the response status code should be 200
    And the response should contain the booking ID
    And the response should match the provided booking details

  Scenario: Update an existing booking
    Given the user connects to the API
    And a booking with ID '1' exists
    And the user has a valid authentication token
    When the user sends a PUT request to '/booking/1' with updated booking data
    Then the response status code should be 200
    And the response should contain the updated booking details

  Scenario: Partially update an existing booking
    Given the user connects to the API
    And a booking with ID '1' exists
    And the user has a valid authentication token
    When the user sends a PATCH request to '/booking/1' with partial booking data
    Then the response status code should be 200
    And the response should contain the partially updated booking details

  Scenario: Delete an existing booking
    Given the user connects to the API
    And a booking with ID '1' exists
    And the user has a valid authentication token
    When the user sends a DELETE request to '/booking/1'
    Then the response status code should be 201

  Scenario: Check API health status
    Given the user connects to the API
    When the user sends a GET request to '/ping'
    Then the response status code should be 201
    And the response body should be 'Created'