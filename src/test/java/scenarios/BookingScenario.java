package scenarios;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import requests.BookingRequests;

public class BookingScenario {

    private static final FeederBuilder<String> csvFeeder = csv("users.csv").circular();

    // Сценарий 1: Легкий (Создание, просмотр и ПОИСК)
    public static ScenarioBuilder browsingLoad = scenario("Load Simulation 1")
        .feed(csvFeeder)
        .exec(BookingRequests.createBooking)
        .pause(1, 3)
        .exec(BookingRequests.getBooking)
        .pause(1)
        .exec(BookingRequests.getBookingByFilter); // <-- НОВЫЙ ШАГ: Поиск по имени из CSV

    // Сценарий 2: Тяжелый (Полный цикл + PATCH)
    public static ScenarioBuilder fullCrudLoad = scenario("Load Simulation 2")
        .feed(csvFeeder)
        .exec(BookingRequests.authenticate) 
        .pause(1)
        .exec(BookingRequests.createBooking) 
        .pause(1)
        .exec(BookingRequests.getBooking) 
        .pause(1)
        .exec(BookingRequests.updateBooking) // PUT (Полное обновление)
        .pause(1)
        .exec(BookingRequests.partialUpdateBooking) // <-- НОВЫЙ ШАГ: PATCH (Частичное обновление)
        .pause(1)
        .exec(BookingRequests.deleteBooking); 
}