package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import scenarios.BookingScenario;

public class LoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://restful-booker.herokuapp.com")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    public LoadSimulation() {
        setUp(
            // 80% trafic (8 users gradually connect within 10 seconds)
            BookingScenario.browsingLoad.injectOpen(
                rampUsers(8).during(10)
            ),
            
            // 20% trafic (2 users gradually connect within 10 seconds)
            BookingScenario.fullCrudLoad.injectOpen(
                rampUsers(2).during(10)
            )
        ).protocols(httpProtocol);
    }
}