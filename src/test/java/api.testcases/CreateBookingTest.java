package api.testcases;

import api.endpoints.BookingEndPoint;

import api.paylods.Booking;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CreateBookingTest {

    static String GlobalID;
    Faker faker;
    Booking userPayload;

    @BeforeClass
    public void generateTestData() {
        faker = new Faker();

        // Initialize BookingPayload object
        userPayload = new Booking();

        // Define maximum total price
        int maxTotalPrice = 1000;

        // Set random values using Faker
        userPayload.setFirstname(faker.name().firstName());
        userPayload.setLastname(faker.name().lastName());
        userPayload.setTotalprice(faker.random().nextInt(maxTotalPrice));
        userPayload.setDepositpaid(faker.random().nextBoolean());

        // Initialize BookingDates object
        Booking.BookingDates bookingDates = new Booking.BookingDates();

        // Calculate check-in and checkout dates
        LocalDate checkin = LocalDate.now().plus(1, ChronoUnit.MONTHS);
        LocalDate checkout = checkin.plus(1, ChronoUnit.MONTHS);

        // Set check-in and checkout dates
        bookingDates.setCheckin(checkin.toString());
        bookingDates.setCheckout(checkout.toString());

        // Set BookingDates
        userPayload.setBookingdates(bookingDates);

        // Set additional needs
        userPayload.setAdditionalneeds(faker.lorem().word());
    }

    @Test(priority = 1)
    public void createBookingTest() {
        Response response = BookingEndPoint.CreateBooking(userPayload);
        response.then().log().all();

        // Assert status code
        int expectedStatusCode = 200;
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        // Assert response body for potential error messages
        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.contains("error"), "Error in response body: " + responseBody);
        GlobalID = response.getSessionId();
        JsonPath jsonPath = new JsonPath(responseBody);
        GlobalID = jsonPath.getString("bookingid");

        System.out.println("This Result is : "+GlobalID);
    }



    @Test(priority = 2)
    public void createBookingWithMaxTotalPrice()
    {
        Booking maxTotalPricePaylods = userPayload;
        maxTotalPricePaylods.setTotalprice(1000);

        Response response = BookingEndPoint.CreateBooking(maxTotalPricePaylods);
                response.then().log().all();

                int expectedStatusCode = 200;
                int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.contains("error"), "Error in response body: " + responseBody);

        JsonPath jsonPath = new JsonPath(responseBody);
        GlobalID = jsonPath.getString("bookingid");
        System.out.println("Booking ID with Max Total Price: " + GlobalID);



    }

    @Test(priority = 3)
    public void verifySuccessfulBookingIdRetrieval()
    {
        Assert.assertNotNull(GlobalID,"Booking ID is not available for retrieval");
        System.out.println("Retrieved Booking ID: " + GlobalID);

    }


    @Test(priority = 4)
    public void createBookingWithInvalidTotalPrice() {
        Booking invalidTotalPricePayload = userPayload;
        invalidTotalPricePayload.setTotalprice(-50);

        Response response = BookingEndPoint.CreateBooking(invalidTotalPricePayload);
        response.then().log().all();

        int expectedStatusCode = 400;
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("totalprice"), "Invalid Total Price error not found in response");
        System.out.println("Invalid Total Price Error: " + responseBody);
    }


    @Test(priority = 5)
    public void createBookingWithMissingLastName() {
        Booking missingLastNamePayload = userPayload;
        missingLastNamePayload.setLastname(null);

        Response response = BookingEndPoint.CreateBooking(missingLastNamePayload);
        response.then().log().all();

        int expectedStatusCode = 400;
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("lastname"), "Missing Last Name error not found in response");
        System.out.println("Missing Last Name Error: " + responseBody);
    }

    @Test(priority = 6)
    public void verifyErrorResponseForInvalidCheckinDate() {
        Booking invalidCheckinDatePayload = userPayload;
        invalidCheckinDatePayload.getBookingdates().setCheckin("2022-01-01"); // Assuming this is an invalid date

        Response response = BookingEndPoint.CreateBooking(invalidCheckinDatePayload);
        response.then().log().all();

        int expectedStatusCode = 400;
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("date"), "Invalid Check-in Date error not found in response");
        System.out.println("Invalid Check-in Date Error: " + responseBody);
    }

    @Test(priority = 7)
    public void createBookingWithBlankAdditionalNeeds() {
        Booking blankAdditionalNeedsPayload = userPayload;
        blankAdditionalNeedsPayload.setAdditionalneeds("");

        Response response = BookingEndPoint.CreateBooking(blankAdditionalNeedsPayload);
        response.then().log().all();

        int expectedStatusCode = 200;
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.contains("error"), "Error in response body: " + responseBody);

        JsonPath jsonPath = new JsonPath(responseBody);
        GlobalID = jsonPath.getString("bookingid");
        System.out.println("Booking ID with Blank Additional Needs: " + GlobalID);
    }


}
