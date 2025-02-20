import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PingTest extends BaseTest {

    @Test
    @Description("Verify that sending a GET request to /ping returns HTTP 200 OK or 201 Created indicating the API is up")
    void testPingEndpoint() {
        Response response = RestAssured.get("/ping");

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 200 || statusCode == 201,
                "Expected status code 200 or 201, but got " + statusCode);
    }
}
