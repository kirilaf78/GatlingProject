package scenarios;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import requests.BookingRequests;

public class BookingScenario {

    // read CSV file. circular() means that when the lines run out, Gatling will start reading the file again
    private static final FeederBuilder<String> csvFeeder = csv("users.csv").circular();

    public static ScenarioBuilder defaultLoad = scenario("Standard booking cycle")
        .feed(csvFeeder) // <-- Connect the feeder at the very beginning of the scenario
        
        .exec(BookingRequests.authenticate)
        .pause(1, 3)
        
        .exec(BookingRequests.createBooking)
        .pause(1, 2)
        
        .exec(BookingRequests.getBooking);
}