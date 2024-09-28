package Fetch.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.*;
import static io.restassured.RestAssured.*;

public class FetchUtils {

    private static final String BASE_URL = ConfigReader.BASE_URL;
    private static final String DIRECT_ENDPOINT = "/direct";
    private static final String ZIP_ENDPOINT = "/zip";
    private static final String API_KEY = ConfigReader.getProperty("api_key");
    private static final String APPID = "appid";

    public static Response fetchByCityAndState(String cityState) {
        Response response = sendRequestWithParams("q", cityState + " US", DIRECT_ENDPOINT);
        System.out.println(formatLocationDetails(extractLocationDetails(response)));
        return response;
    }

    public static Response fetchByZip(String zipCode) {

        Response response = sendRequestWithParams("zip", zipCode + ",US", ZIP_ENDPOINT);
        Map<String, Object> locationDetails = extractLocationDetails(response);
        System.out.println("Location Details by Zip = " + locationDetails);
        return response;
    }

    private static Response sendRequestWithParams(String queryParamKey, String queryParamValue, String endpoint) {
        return given()
                .accept(ContentType.JSON)
                .queryParam(queryParamKey, queryParamValue)
                .queryParam(APPID, API_KEY)
                .when().get(BASE_URL + endpoint)
                .then()
                .extract().response();
    }

    public static Map<String, Object> extractLocationDetails(Response response) {

        Object jsonResponse = response.jsonPath().get("$");
        String message = response.jsonPath().getString("message");

        switch (response.getStatusCode()) {
            case 200:

                if (jsonResponse instanceof Map) {
                    return (Map<String, Object>) jsonResponse;
                }

                if (jsonResponse instanceof List<?>) {
                    List<Map<String, Object>> locations = (List<Map<String, Object>>) jsonResponse;
                    if (!locations.isEmpty()) {
                        return locations.getFirst();
                    } else {
                        return new HashMap<>();
                    }
                }
                throw new RuntimeException("No Data Found: " + response.asString());

            case 400:
                System.out.println("Bad Request - 400: " + message);
                return createErrorMap("400", message);
            case 401:
                System.out.println("Unauthorized Access - 401: " + message);
                return createErrorMap("401", message);
            case 404:
                System.out.println("Not Found - 404: " + message);
                return createErrorMap("404", message);
            case 500:
                System.out.println("Internal Server Error - 500: " + message);
                return createErrorMap("500", message);

            default:
                throw new RuntimeException("Unexpected Response with status code: " + response.getStatusCode() + " - " + response.asString());
        }
    }


    private static Map<String, Object> createErrorMap(String errorType, String message) {
        Map<String , Object> errorMap = new HashMap<>();
        errorMap.put("error", errorType);
        errorMap.put("message", message != null && !message.isEmpty() ? message : "No message");
        return errorMap;
    }

    public static String formatLocationDetails(Map<String, Object> location) {
        return String.format(
                "Place: %s, Latitude: %s, Longitude: %s, Country: %s",
                location.get("name"),
                location.get("lat"),
                location.get("lon"),
                location.get("country")
        );
    }
}
