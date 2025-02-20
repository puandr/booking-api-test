import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokenTest {

    @Test
    @Description("Verify that providing valid credentials returns a valid auth token")
    void testCreateAuthTokenWithValidCredentials() {
        String requestBody = "{ \"username\": \"admin\", \"password\": \"password123\" }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("https://restful-booker.herokuapp.com/auth");

        assertEquals(200, response.getStatusCode(), "Expected HTTP status code 200");

        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }
}
