import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateBookingTest extends BaseTest{

    @Test
    @Description("Verify that providing a valid token and updated fields returns HTTP 200 OK and an updated booking object")
    void testUpdateBookingWithValidToken() {
        String authPayload = "{ \"username\": \"admin\", \"password\": \"password123\" }";
        Response authResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .post("/auth");
        assertEquals(200, authResponse.getStatusCode(), "Auth token request failed");
        String token = authResponse.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");

        int bookingId = 1;

        String updatePayload = "{\n" +
                "    \"firstname\" : \"UpdatedFirstName\",\n" +
                "    \"lastname\" : \"UpdatedLastName\",\n" +
                "    \"totalprice\" : 150,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "         \"checkin\" : \"2023-01-01\",\n" +
                "         \"checkout\" : \"2023-01-10\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";

        Response updateResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(updatePayload)
                .put("/booking/" + bookingId);

        assertEquals(200, updateResponse.getStatusCode(), "Expected HTTP status code 200");

        String updatedFirstname = updateResponse.jsonPath().getString("firstname");
        String updatedLastname = updateResponse.jsonPath().getString("lastname");
        int updatedTotalprice = updateResponse.jsonPath().getInt("totalprice");
        boolean updatedDepositpaid = updateResponse.jsonPath().getBoolean("depositpaid");
        String updatedCheckin = updateResponse.jsonPath().getString("bookingdates.checkin");
        String updatedCheckout = updateResponse.jsonPath().getString("bookingdates.checkout");
        String updatedAdditionalneeds = updateResponse.jsonPath().getString("additionalneeds");

        assertEquals("UpdatedFirstName", updatedFirstname, "Firstname was not updated correctly");
        assertEquals("UpdatedLastName", updatedLastname, "Lastname was not updated correctly");
        assertEquals(150, updatedTotalprice, "Total price was not updated correctly");
        assertFalse(updatedDepositpaid, "Deposit paid flag was not updated correctly");
        assertEquals("2023-01-01", updatedCheckin, "Checkin date was not updated correctly");
        assertEquals("2023-01-10", updatedCheckout, "Checkout date was not updated correctly");
        assertEquals("Lunch", updatedAdditionalneeds, "Additional needs was not updated correctly");
    }
}
