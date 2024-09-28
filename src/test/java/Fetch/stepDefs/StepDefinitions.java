package Fetch.stepDefs;

import Fetch.utils.FetchUtils;
import Fetch.utils.US_States;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

public class StepDefinitions {

    private Response cityStateResponse;
    private Response zipResponse;
    private String cityState;
    private String zipCode;

    @Given("I want to fetch geolocation data for {string}")
    public void i_want_to_fetch_geolocation_data_for(String cityState) {
        cityStateResponse = FetchUtils.fetchByCityAndState(cityState);
        String expectedCity = cityState.split(",")[0].trim();

        Assert.assertEquals(expectedCity, cityStateResponse.jsonPath().getString("[0].name"));
    }

    @When("I perform the API call")
    public void i_perform_the_api_call() {
        // API call is already performed in the @Given step.
        // This step exists for BDD clarity.
    }

    @Then("I should see the correct geolocation data for {string}")
    public void i_should_see_the_correct_geolocation_data_for(String expectedLocation) {
        cityStateResponse = FetchUtils.fetchByCityAndState(expectedLocation);
        zipResponse = FetchUtils.fetchByZip(expectedLocation);
        if (expectedLocation.contains(",")) {
            // CityState scenario
            String expectedCity = expectedLocation.split(",")[0].trim();
            Assert.assertEquals(expectedCity, cityStateResponse.jsonPath().getString("[0].name"));
        } else {
            // ZIP Code scenario
            Assert.assertEquals(expectedLocation, zipResponse.jsonPath().getString("zip"));
        }
    }

    @Given("I want to fetch geolocation data for the zip code {string}")
    public void i_want_to_fetch_geolocation_data_for_the_zip_code(String validZipCode) {
        zipResponse = FetchUtils.fetchByZip(validZipCode);
        Assert.assertEquals(validZipCode, zipResponse.jsonPath().get("zip"));
    }

    @Given("I want to fetch geolocation data for the invalid zip code {string}")
    public void iWantToFetchGeolocationDataForTheInvalidZipCode(String invalidZipCode) {
        zipResponse = FetchUtils.fetchByZip(invalidZipCode);
        Assert.assertEquals(404, zipResponse.getStatusCode());
        Assert.assertEquals("not found", zipResponse.jsonPath().getString("message"));
    }


    @Then("I should see an error on response for invalid {string} format")
    public void iShouldSeeAnErrorOnResponseForInvalidFormat(String invalidZipCode) {
        zipResponse = FetchUtils.fetchByZip(invalidZipCode);
        boolean isError = FetchUtils.extractLocationDetails(zipResponse.andReturn()).containsValue("404");
        Assert.assertTrue(isError);
    }


    @Given("I want to fetch geolocation data for {string}, {string}")
    public void i_want_to_fetch_geolocation_data_for(String cityState, String zipCode) {
        this.cityState = cityState;
        this.zipCode = zipCode;
        cityStateResponse = FetchUtils.fetchByCityAndState(cityState);
        zipResponse = FetchUtils.fetchByZip(zipCode);
        Assert.assertNotNull(cityStateResponse);
        Assert.assertNotNull(zipResponse);
    }


    @When("I perform the API call for multiple locations")
    public void i_perform_the_api_call_for_multiple_locations() {
        // API call is already performed in the @Given step.
        // This step exists for BDD clarity.
    }

    @Then("I should see the correct geolocation data for each input")
    public void i_should_see_the_correct_geolocation_data_for_each_input() {

        //Assert City
        String expectedCity = cityState.split(",")[0].trim();
        Assert.assertEquals(expectedCity, cityStateResponse.jsonPath().getString("[0].name"));

        //Assert State
        US_States abbreviation = US_States.fromAbbreviation(cityState.split(",")[1].trim());
        String expectedState = abbreviation.getState();
        Assert.assertEquals(expectedState, cityStateResponse.jsonPath().getString("[0].state"));

        //Assert Zip Code
        Assert.assertEquals(zipCode, zipResponse.jsonPath().getString("zip"));
    }


    @Given("I want to fetch geolocation data for {string} and {string} with an invalid format and input")
    public void iWantToFetchGeolocationDataForAndWithAnInvalidFormatAndInput(String cityState, String zipCode) {
        cityStateResponse = FetchUtils.fetchByCityAndState(cityState);
        zipResponse = FetchUtils.fetchByZip(zipCode);
        Assert.assertNotNull(cityStateResponse);
        Assert.assertNotNull(zipResponse);
    }


    @Then("I should see an error response for invalid inputs with {string} and error message {string}")
    public void iShouldSeeAnErrorResponseForInvalidInputsWithAndErrorMessage(String expectedErrorCode, String expectedMessage) {

        Map<String, Object> responseData = FetchUtils.extractLocationDetails(zipResponse);
        Assert.assertEquals(expectedErrorCode, String.valueOf(zipResponse.getStatusCode()));
        Assert.assertEquals(expectedMessage, String.valueOf(responseData.get("message")));

    }

    @And("I should see the correct geolocation data for valid inputs with {string} and {string}")
    public void iShouldSeeTheCorrectGeolocationDataForValidInputsWithAnd(String cityState, String zipCode) {
        String expectedCity = cityState.split(",")[0].trim();

        if (zipResponse.getStatusCode() == 200) {
            System.out.println("Success: valid zip code " + zipResponse.getStatusCode());
            Assert.assertEquals(expectedCity, cityStateResponse.jsonPath().getString("[0].name"));
            Assert.assertEquals(zipCode, zipResponse.jsonPath().getString("zip"));
        } else {
            System.out.println("Skipping correct geolocation data validation on invalid input for: " + cityState + " and " + zipCode);
        }
    }
}

