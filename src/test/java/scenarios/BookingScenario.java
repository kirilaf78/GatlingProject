package scenarios;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import requests.BookingRequests;

public class BookingScenario {

    private static final FeederBuilder<String> csvFeeder = csv("users.csv").circular();

    // Сценарий 1: Легкий (Создание и просмотр) - Не требует авторизации
    public static ScenarioBuilder browsingLoad = scenario("Создание и просмотр (Browsing)")
        .feed(csvFeeder)
        .exec(BookingRequests.createBooking)
        .pause(1, 3)
        .exec(BookingRequests.getBooking);

    // Сценарий 2: Тяжелый (Полный цикл) - Требует токен для PUT и DELETE
    public static ScenarioBuilder fullCrudLoad = scenario("Полный цикл (Full CRUD)")
        .feed(csvFeeder)
        .exec(BookingRequests.authenticate) // 1. Получаем токен
        .pause(1)
        .exec(BookingRequests.createBooking) // 2. Создаем бронь
        .pause(1)
        .exec(BookingRequests.getBooking) // 3. Читаем
        .pause(1)
        .exec(BookingRequests.updateBooking) // 4. Обновляем (PUT)
        .pause(1)
        .exec(BookingRequests.deleteBooking); // 5. Удаляем (DELETE)
}