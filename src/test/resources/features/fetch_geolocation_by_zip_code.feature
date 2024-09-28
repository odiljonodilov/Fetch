@byZipCode
Feature: Fetch Geolocation by Zip Code
  As a user of the Geolocation utility
  I want to fetch geolocation data by providing a zip code
  So that I can get the latitude, longitude, and place name

  Scenario Outline: Fetch geolocation for a valid zip code
    Given I want to fetch geolocation data for the zip code "<zipCode>"
    When I perform the API call
    Then I should see the correct geolocation data for "<zipCode>"
    Examples:
      | zipCode |
      | 82001   |
      | 83702   |
      | 33101   |
      | 85201   |
      | 77001   |


  Scenario Outline: Fetch geolocation for an invalid zip code
    Given I want to fetch geolocation data for the invalid zip code "<InvalidZipCode>"
    When I perform the API call
    Then I should see an error on response for invalid "<InvalidZipCode>" format
    Examples:
      | InvalidZipCode |
      | 00000          |
      | 99999          |
      | 9L9L9L         |
      | @L9l-          |
      | 123            |





