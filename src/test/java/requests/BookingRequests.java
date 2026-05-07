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

    // 4. Update Booking (PUT)
    public static HttpRequestActionBuilder updateBooking = 
        http("4. Update Booking")
            .put("/booking/#{bookingId}")
            .header("Cookie", "token=#{authToken}") // pass our saved token
            .body(StringBody("{" +
                "\"firstname\": \"#{firstname}\"," + 
                "\"lastname\": \"#{lastname}\"," +   
                "\"totalprice\": 999," + 
                "\"depositpaid\": false,"+
                "\"bookingdates\": {" +
                    "\"checkin\": \"2025-05-01\"," +
                    "\"checkout\": \"2025-05-10\"" +
                "}," +
                "\"additionalneeds\": \"Dinner\"" + // changed Breakfast to Dinner
            "}"))
            .check(status().is(200));

    // 5. Delete Booking (DELETE)
    public static HttpRequestActionBuilder deleteBooking = 
        http("5. Delete Booking")
            .delete("/booking/#{bookingId}")
            .header("Cookie", "token=#{authToken}") // pass our saved token
            .check(status().is(201)); // API Restful Booker returns status 201 (Created) on successful deletion
}