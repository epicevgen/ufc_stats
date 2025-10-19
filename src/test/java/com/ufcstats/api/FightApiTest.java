package com.ufcstats.api;

import com.ufcstats.model.enums.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * API тесты для Fight endpoints с использованием REST Assured
 * 
 * Покрывает:
 * - GET /api/fights - получение списка боев
 * - GET /api/fights/{id} - получение конкретного боя
 * - POST /api/fights - создание нового боя
 * - PUT /api/fights/{id} - обновление боя
 * - DELETE /api/fights/{id} - удаление боя
 */
@Slf4j
public class FightApiTest extends BaseApiTest {

    @Test
    void testGetAllFights() {
        logTestInfo("GET /api/fights", "Получение списка всех боев");

        Response response = givenGetRequest()
                .when()
                .get("/api/fights")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("content", notNullValue())
                .body("totalElements", greaterThanOrEqualTo(0))
                .extract()
                .response();

        log.info("Получено боев: {}", (Object) response.path("totalElements"));
        assertNotNull(response.path("content"));
    }

    @Test
    void testGetFightById() {
        logTestInfo("GET /api/fights/{id}", "Получение боя по ID");

        // Сначала создаем бой для тестирования
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
        assertNotNull(fightId);

        // Теперь получаем созданный бой
        givenGetRequest()
                .when()
                .get("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("id", equalTo(fightId.intValue()))
                .body("myFighter", equalTo(fightData.get("myFighter")))
                .body("opponent", equalTo(fightData.get("opponent")))
                .body("result", equalTo(fightData.get("result")));

        // Очистка - удаляем созданный бой
        givenDeleteRequest()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void testCreateFight() {
        logTestInfo("POST /api/fights", "Создание нового боя");

        Map<String, Object> fightData = createTestFightData();

        Response response = givenPostRequest()
                .body(fightData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json")
                .body("id", notNullValue())
                .body("myFighter", equalTo(fightData.get("myFighter")))
                .body("opponent", equalTo(fightData.get("opponent")))
                .body("result", equalTo(fightData.get("result")))
                .body("fightMode", equalTo(fightData.get("fightMode")))
                .body("weightClass", equalTo(fightData.get("weightClass")))
                .extract()
                .response();

        Long fightId = response.path("id");
        log.info("Создан бой с ID: {}", fightId);

        // Очистка
        givenDeleteRequest()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void testUpdateFight() {
        logTestInfo("PUT /api/fights/{id}", "Обновление существующего боя");

        // Создаем бой для обновления
        Map<String, Object> originalData = createTestFightData();
        
        Response createResponse = givenPostRequest()
                .body(originalData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();

        Long fightId = createResponse.path("id");

        // Подготавливаем данные для обновления
        Map<String, Object> updateData = new HashMap<>(originalData);
        updateData.put("myFighter", "Обновленный Боец");
        updateData.put("opponent", "Обновленный Соперник");
        updateData.put("result", FightResult.LOSS.name());
        updateData.put("notes", "Обновленные заметки");

        // Обновляем бой
        givenPutRequest()
                .body(updateData)
                .when()
                .put("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("id", equalTo(fightId.intValue()))
                .body("myFighter", equalTo(updateData.get("myFighter")))
                .body("opponent", equalTo(updateData.get("opponent")))
                .body("result", equalTo(updateData.get("result")))
                .body("notes", equalTo(updateData.get("notes")));

        // Очистка
        givenDeleteRequest()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void testDeleteFight() {
        logTestInfo("DELETE /api/fights/{id}", "Удаление боя");

        // Создаем бой для удаления
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

        // Удаляем бой
        givenDeleteRequest()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Проверяем, что бой действительно удален
        givenGetRequest()
                .when()
                .get("/api/fights/" + fightId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testGetFightNotFound() {
        logTestInfo("GET /api/fights/{id} - 404", "Получение несуществующего боя");

        givenGetRequest()
                .when()
                .get("/api/fights/99999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testCreateFightWithInvalidData() {
        logTestInfo("POST /api/fights - 400", "Создание боя с некорректными данными");

        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("myFighter", ""); // Пустое имя
        invalidData.put("opponent", null); // Null значение

        givenPostRequest()
                .body(invalidData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Создает тестовые данные для боя
     */
    private Map<String, Object> createTestFightData() {
        Map<String, Object> fightData = new HashMap<>();
        fightData.put("fightDate", LocalDateTime.now().toString());
        fightData.put("fightMode", FightMode.MMA.name());
        fightData.put("season", 1);
        fightData.put("result", FightResult.WIN.name());
        fightData.put("method", FightMethod.DECISION.name());
        fightData.put("roundsPlayed", 3);
        fightData.put("ratingPoints", 85);
        fightData.put("rankingPosition", 5);
        fightData.put("weightClass", WeightClass.LIGHTWEIGHT.name());
        fightData.put("myFighter", "Тестовый Боец " + System.currentTimeMillis());
        fightData.put("opponent", "Тестовый Соперник " + System.currentTimeMillis());
        fightData.put("notes", "Тестовый бой для API тестирования");
        
        return fightData;
    }
}
