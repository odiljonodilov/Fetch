@byCityAndState
Feature: Fetch Geolocation by City and State
  As a user of the Geolocation utility
  I want to fetch geolocation data by providing a city and state
  So that I can get the latitude, longitude, and place name

  Scenario Outline: Fetch geolocation for a single city and state
    Given I want to fetch geolocation data for "<CityState>"
    When I perform the API call
    Then I should see the correct geolocation data for "<CityState>"
    Examples:
      | CityState         |
      | Beaverton, OR     |
      | Orlando, FL       |
      | Phoenix, AZ       |
      | Austin, TX        |
      | San Francisco, FL |


