import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeleteBookingTest extends BaseTest{

    @Test
    @Description("Verify that providing a valid token and a correct booking ID yields HTTP 200/201 and confirms the booking is removed")
    void testDeleteBookingWithValidToken() {
        String createBookingPayload = "{\n" +
                "    \"firstname\" : \"Vasilious\",\n" +
                "    \"lastname\" : \"Pupkinos\",\n" +
                "    \"totalprice\" : 200,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "         \"checkin\" : \"2024-01-01\",\n" +
                "         \"checkout\" : \"2024-01-10\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Dinner\"\n" +
                "}";

        Response createResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createBookingPayload)
                .post("/booking");

        int createStatus = createResponse.getStatusCode();
        assertTrue(createStatus == 200 || createStatus == 201, "Booking creation failed with status code " + createStatus);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        assertTrue(bookingId > 0, "Invalid booking ID");

        String authPayload = "{ \"username\": \"admin\", \"password\": \"password123\" }";
        Response authResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .post("/auth");
        assertEquals(200, authResponse.getStatusCode(), "Auth token request failed");
        String token = authResponse.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");

        Response deleteResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .delete("/booking/" + bookingId);

        int deleteStatus = deleteResponse.getStatusCode();
        assertTrue(deleteStatus == 200 || deleteStatus == 201,
                "Expected delete status 200/201 but got " + deleteStatus);

        Response getDeletedResponse = RestAssured.given()
                .header("Accept", "application/json")
                .get("/booking/" + bookingId);

        assertEquals(404, getDeletedResponse.getStatusCode(),
                "Expected GET on deleted booking to return 404, but got " + getDeletedResponse.getStatusCode());
    }
}
