package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import scenarios.BookingScenario; // Import our scenario

public class LoadSimulation extends Simulation {

    // 1. Basic HTTP configuration (common to all requests)
    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://restful-booker.herokuapp.com")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    // 2. Injection Profile
    public LoadSimulation() {
        setUp(
            // Take our ready scenario from the BookingScenario class
            BookingScenario.defaultLoad.injectOpen(
                atOnceUsers(1),         // Warm-up: 1 user makes requests immediately
                rampUsers(5).during(10) // Then another 5 users gradually connect within 10 seconds
            )
        ).protocols(httpProtocol);
    }
}