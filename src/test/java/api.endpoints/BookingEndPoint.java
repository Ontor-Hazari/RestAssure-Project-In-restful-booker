package api.endpoints;
import static io.restassured.RestAssured.given;

import api.paylods.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BookingEndPoint  {

   @Test
    public static Response CreateBooking(Booking payloads)
    {

        Response response = RestAssured.given( )
                .contentType(ContentType.JSON)
                .body(payloads)
                .when()
                .post(Routes.CreateBooking);

        return response;


    }


    public static Response getBooking(String bookingId) {
        String getBookingUrl = Routes.GetBooking.replace(":id", bookingId);

        Response response = RestAssured.given()
                .when()
                .get(getBookingUrl);

        return response;
    }





}