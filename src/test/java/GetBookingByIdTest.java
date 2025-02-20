import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GetBookingByIdTest {

    @Test
    @Description("Verify that requesting an existing booking ID returns HTTP 200 OK and contains booking details with valid values")
    void testGetBookingById() {
        int bookingId = 1;

        Response response = RestAssured.given()
                .header("Accept", "application/json")
                .get("https://restful-booker.herokuapp.com/booking/" + bookingId);

        assertEquals(200, response.getStatusCode(), "Expected HTTP status code 200");

        String firstname = response.jsonPath().getString("firstname");
        String lastname = response.jsonPath().getString("lastname");
        Integer totalprice = response.jsonPath().getInt("totalprice");
        Boolean depositpaid = response.jsonPath().getBoolean("depositpaid");
        String checkin = response.jsonPath().getString("bookingdates.checkin");
        String checkout = response.jsonPath().getString("bookingdates.checkout");
        String additionalneeds = response.jsonPath().getString("additionalneeds");

        assertNotNull(firstname, "Firstname should not be null");
        assertFalse(firstname.trim().isEmpty(), "Firstname should not be empty");

        assertNotNull(lastname, "Lastname should not be null");
        assertFalse(lastname.trim().isEmpty(), "Lastname should not be empty");

        assertNotNull(totalprice, "Total price should not be null");
        assertTrue(totalprice > 0, "Total price should be a positive number");

        assertNotNull(depositpaid, "Deposit paid flag should not be null");

        assertNotNull(checkin, "Checkin date should not be null");
        assertFalse(checkin.trim().isEmpty(), "Checkin date should not be empty");

        assertNotNull(checkout, "Checkout date should not be null");
        assertFalse(checkout.trim().isEmpty(), "Checkout date should not be empty");


    }
}
