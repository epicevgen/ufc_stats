package com.ufcstats.api;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * API тесты для Statistics endpoints
 * 
 * Покрывает:
 * - GET /statistics - получение статистики
 * - GET /statistics с фильтрами - фильтрация статистики
 */
@Slf4j
public class StatisticsApiTest extends BaseApiTest {

    @Test
    void testGetStatistics() {
        logTestInfo("GET /statistics", "Получение статистики боев");

        Response response = givenGetRequest()
                .when()
                .get("/statistics")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8")
                .extract()
                .response();

        // Проверяем, что страница содержит основные элементы статистики
        String responseBody = response.asString();
        assertTrue(responseBody.contains("Статистика боев"));
        assertTrue(responseBody.contains("Основные показатели"));
        assertTrue(responseBody.contains("Статистика побед и поражений"));
        
        log.info("Страница статистики загружена успешно");
    }

    @Test
    void testGetStatisticsWithFilters() {
        logTestInfo("GET /statistics с фильтрами", "Получение статистики с фильтрацией");

        // Тест с фильтром по режиму боя
        givenGetRequest()
                .param("fightModeFilter", "MMA")
                .when()
                .get("/statistics")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");

        // Тест с фильтром по сезону
        givenGetRequest()
                .param("seasonFilter", "1")
                .when()
                .get("/statistics")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");

        // Тест с комбинированными фильтрами
        givenGetRequest()
                .param("fightModeFilter", "STANCE")
                .param("seasonFilter", "2")
                .when()
                .get("/statistics")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");

        log.info("Фильтрация статистики работает корректно");
    }

    @Test
    void testGetFightsPage() {
        logTestInfo("GET /fights", "Получение страницы списка боев");

        Response response = givenGetRequest()
                .when()
                .get("/fights")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8")
                .extract()
                .response();

        String responseBody = response.asString();
        assertTrue(responseBody.contains("Список боев"));
        assertTrue(responseBody.contains("Добавить бой"));
        
        log.info("Страница списка боев загружена успешно");
    }

    @Test
    void testGetFightsWithSearch() {
        logTestInfo("GET /fights с поиском", "Поиск боев");

        // Тест поиска
        givenGetRequest()
                .param("search", "тест")
                .when()
                .get("/fights")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");

        // Тест фильтрации по результату
        givenGetRequest()
                .param("resultFilter", "WIN")
                .when()
                .get("/fights")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");

        log.info("Поиск и фильтрация боев работает корректно");
    }

    @Test
    void testGetFightDetails() {
        logTestInfo("GET /fights/{id}", "Получение деталей боя");

        // Сначала создаем тестовый бой
        Map<String, Object> fightData = createTestFightData();
        
        Response createResponse = givenPostRequest()
                .body(fightData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();

        Long fightId = createResponse.path("id");

        // Получаем детали боя
        Response response = givenGetRequest()
                .when()
                .get("/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8")
                .extract()
                .response();

        String responseBody = response.asString();
        assertTrue(responseBody.contains("Детали боя"));
        assertTrue(responseBody.contains(fightData.get("myFighter").toString()));
        assertTrue(responseBody.contains(fightData.get("opponent").toString()));

        // Очистка
        givenDeleteRequest()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        log.info("Детали боя загружены успешно");
    }

    @Test
    void testGetFightDetailsNotFound() {
        logTestInfo("GET /fights/{id} - 404", "Получение несуществующего боя");

        givenGetRequest()
                .when()
                .get("/fights/99999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Создает тестовые данные для боя
     */
    private Map<String, Object> createTestFightData() {
        Map<String, Object> fightData = new HashMap<>();
        fightData.put("fightDate", java.time.LocalDateTime.now().toString());
        fightData.put("fightMode", "MMA");
        fightData.put("season", 1);
        fightData.put("result", "WIN");
        fightData.put("method", "DECISION");
        fightData.put("roundsPlayed", 3);
        fightData.put("ratingPoints", 85);
        fightData.put("rankingPosition", 5);
        fightData.put("weightClass", "LIGHTWEIGHT");
        fightData.put("myFighter", "API Тест Боец " + System.currentTimeMillis());
        fightData.put("opponent", "API Тест Соперник " + System.currentTimeMillis());
        fightData.put("notes", "Тестовый бой для API тестирования");
        
        return fightData;
    }
}
