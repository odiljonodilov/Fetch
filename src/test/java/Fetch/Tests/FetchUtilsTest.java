package Fetch.Tests;

import Fetch.utils.FetchUtils;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchUtilsTest {
    @Test
    public void testFetchByZip() {
        Response response = FetchUtils.fetchByZip("60625");
        Map<String, Object> actualData = FetchUtils.extractLocationDetails(response);

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("zip", "60625");
        expectedData.put("name", "Chicago");
        expectedData.put("lat", 41.9703f);
        expectedData.put("lon", -87.7042f);
        expectedData.put("country", "US");
        Assert.assertEquals(expectedData, actualData);

    }

    @Test
    public void testFetchByCityState() {
        Response response = FetchUtils.fetchByCityAndState("New York, NY");
        List<Map<String, Object>> locations = response.jsonPath().getList("$");
        Map<String, Object> firstLocation = locations.getFirst();
        Assert.assertFalse("The locations list should not be empty", locations.isEmpty());
        Assert.assertEquals("New York", firstLocation.get("name"));
    }
}
