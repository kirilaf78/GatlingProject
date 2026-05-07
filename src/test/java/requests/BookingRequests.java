package requests;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class BookingRequests {

    // 1. Authorization Request
    public static HttpRequestActionBuilder authenticate = 
        http("1. Auth")
            .post("/auth")
            .body(StringBody("{\"username\": \"admin\", \"password\": \"password123\"}"))
            .check(status().is(200))
            .check(jsonPath("$.token").saveAs("authToken")); // Save the token in the authToken variable

// 2. Create booking with dynamic data
    public static HttpRequestActionBuilder createBooking = 
        http("2. Create Booking")
            .post("/booking")
            .body(StringBody("{" +
                "\"firstname\": \"#{firstname}\"," + // Taken from CSV
                "\"lastname\": \"#{lastname}\"," +   // Taken from CSV
                "\"totalprice\": #{randomInt(100,500)}," + // Built-in random number generator Gatling
                "\"depositpaid\": true," +
                "\"bookingdates\": {" +
                    "\"checkin\": \"2025-05-01\"," +
                    "\"checkout\": \"2025-05-10\"" +
                "}," +
                "\"additionalneeds\": \"Breakfast\"" +
            "}"))
            .check(status().is(200))
            .check(jsonPath("$.bookingid").saveAs("bookingId"));
    // 3. Get Booking Request
    public static HttpRequestActionBuilder getBooking = 
        http("3. Get Booking")
            .get("/booking/#{bookingId}") // Insert the ID saved in the previous step
            .check(status().is(200));
}