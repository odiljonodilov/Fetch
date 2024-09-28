@multipleInputs
Feature: Fetch Geolocation for Multiple Inputs
  As a user of the Geolocation utility
  I want to fetch geolocation data for multiple inputs at once
  So that I can get the latitude, longitude, and place name for each

  Scenario Outline: Fetch geolocation for multiple city/state and zip code combinations
    Given I want to fetch geolocation data for "<CityState>", "<zipCode>"
    When I perform the API call for multiple locations
    Then I should see the correct geolocation data for each input
    Examples:
      | CityState   | zipCode |
      | Chicago, IL | 60625   |
      | Seattle,WA  | 98101   |

@invalidInputs
  Scenario Outline: Fetch geolocation for a mix of valid and invalid inputs
    Given I want to fetch geolocation data for "<CityState>" and "<ZipCode>" with an invalid format and input
    When I perform the API call for multiple locations
    Then I should see an error response for invalid inputs with "<ExpectedStatusCode>" and error message "<ExpectedErrorMessage>"
    And I should see the correct geolocation data for valid inputs with "<CityState>" and "<ZipCode>"
    Examples:
      | CityState                             | ZipCode | ExpectedStatusCode | ExpectedErrorMessage |
      | SuperLongCityNameThatExceedsLimit, OR | 00345   | 404                | not found            |
      | Unknown, XX                           | 00000   | 404                | not found            |
      | InvalidCity, XX                       | ABCDE   | 404                | not found            |
      | Phoenix, AZ                           |         | 400                | invalid zip code     |
      |                                       | 99999   | 404                | not found            |
      | , 99999                               | 99999   | 404                | not found            |
      | Beaverton, OR                         | ABC12   | 404                | not found            |
      | #$%^&*City, AZ                        | #$%^&   | 400                | bad params           |
      | 12345, NY                             | 10000   | 404                | not found            |
      |                                       |         | 400                | invalid zip code     |
      | Seattle, WA                           | 98101   | 200                | null                 |
      | Chicago, IL                           | 60625   | 200                | null                 |
