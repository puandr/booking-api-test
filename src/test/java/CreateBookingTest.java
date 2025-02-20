import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreateBookingTest extends BaseTest{

    @Test
    @Description("Verify that using valid JSON with all mandatory fields returns HTTP 200 OK (or 201) and the created booking data")
    void testCreateBookingWithValidJson() {
        String requestBody = "{\n" +
                "  \"firstname\": \"Takinado\",\n" +
                "  \"lastname\": \"Vsegovorat\",\n" +
                "  \"totalprice\": 111,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2018-01-01\",\n" +
                "    \"checkout\": \"2019-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/booking");

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 200 || statusCode == 201,
                "Expected HTTP status code 200 or 201, but got: " + statusCode);

        int bookingId = response.jsonPath().getInt("bookingid");
        assertTrue(bookingId > 0, "Booking ID should be a positive integer");

        String firstname = response.jsonPath().getString("booking.firstname");
        String lastname = response.jsonPath().getString("booking.lastname");
        int totalprice = response.jsonPath().getInt("booking.totalprice");
        boolean depositpaid = response.jsonPath().getBoolean("booking.depositpaid");
        String checkin = response.jsonPath().getString("booking.bookingdates.checkin");
        String checkout = response.jsonPath().getString("booking.bookingdates.checkout");
        String additionalneeds = response.jsonPath().getString("booking.additionalneeds");

        assertEquals("Takinado", firstname, "Firstname does not match");
        assertEquals("Vsegovorat", lastname, "Lastname does not match");
        assertEquals(111, totalprice, "Total price does not match");
        assertTrue(depositpaid, "Deposit should be true");
        assertEquals("2018-01-01", checkin, "Checkin date does not match");
        assertEquals("2019-01-01", checkout, "Checkout date does not match");
        assertEquals("Breakfast", additionalneeds, "Additional needs does not match");
    }
}
