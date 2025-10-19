package com.ufcstats.api;

import com.ufcstats.model.enums.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Утилиты для API тестирования
 * 
 * Содержит вспомогательные методы для:
 * - Создания тестовых данных
 * - Выполнения общих операций
 * - Проверки ответов
 */
@Slf4j
public class ApiTestUtils {

    /**
     * Создает тестовые данные для боя
     */
    public static Map<String, Object> createFightData() {
        return createFightData("Тестовый Боец", "Тестовый Соперник");
    }

    /**
     * Создает тестовые данные для боя с указанными именами
     */
    public static Map<String, Object> createFightData(String myFighter, String opponent) {
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
        fightData.put("myFighter", myFighter + " " + System.currentTimeMillis());
        fightData.put("opponent", opponent + " " + System.currentTimeMillis());
        fightData.put("notes", "Тестовый бой для API тестирования");
        
        return fightData;
    }

    /**
     * Создает бой через API и возвращает его ID
     */
    public static Long createFightViaApi(Map<String, Object> fightData) {
        Response response = given()
                .contentType("application/json")
                .body(fightData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long fightId = response.path("id");
        log.info("Создан бой с ID: {}", fightId);
        return fightId;
    }

    /**
     * Удаляет бой через API
     */
    public static void deleteFightViaApi(Long fightId) {
        given()
                .when()
                .delete("/api/fights/" + fightId)
                .then()
                .statusCode(204);
        
        log.info("Удален бой с ID: {}", fightId);
    }

    /**
     * Создает и сразу удаляет бой (для тестов, которые не требуют сохранения)
     */
    public static Long createAndCleanupFight(Map<String, Object> fightData) {
        Long fightId = createFightViaApi(fightData);
        
        // Добавляем shutdown hook для очистки
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                deleteFightViaApi(fightId);
            } catch (Exception e) {
                log.warn("Не удалось удалить тестовый бой {}: {}", fightId, e.getMessage());
            }
        }));
        
        return fightId;
    }

    /**
     * Проверяет, что ответ содержит ожидаемые поля
     */
    public static void assertFightResponse(Response response, Map<String, Object> expectedData) {
        response.then()
                .body("id", notNullValue())
                .body("myFighter", equalTo(expectedData.get("myFighter")))
                .body("opponent", equalTo(expectedData.get("opponent")))
                .body("result", equalTo(expectedData.get("result")))
                .body("fightMode", equalTo(expectedData.get("fightMode")))
                .body("weightClass", equalTo(expectedData.get("weightClass")));
    }

    /**
     * Создает данные для боя с поражением
     */
    public static Map<String, Object> createLossFightData() {
        Map<String, Object> fightData = createFightData("Проигравший Боец", "Победивший Соперник");
        fightData.put("result", FightResult.LOSS.name());
        fightData.put("method", FightMethod.KNOCKOUT.name());
        fightData.put("ratingPoints", 70);
        fightData.put("rankingPosition", 8);
        return fightData;
    }

    /**
     * Создает данные для боя с ничьей
     */
    public static Map<String, Object> createDrawFightData() {
        Map<String, Object> fightData = createFightData("Боец Ничья", "Соперник Ничья");
        fightData.put("result", FightResult.DRAW.name());
        fightData.put("method", FightMethod.DECISION.name());
        fightData.put("ratingPoints", 80);
        fightData.put("rankingPosition", 6);
        return fightData;
    }

    /**
     * Создает данные для боя в режиме Стойка
     */
    public static Map<String, Object> createStanceFightData() {
        Map<String, Object> fightData = createFightData("Стойка Боец", "Стойка Соперник");
        fightData.put("fightMode", FightMode.STANCE.name());
        fightData.put("weightClass", WeightClass.MIDDLEWEIGHT.name());
        return fightData;
    }

    /**
     * Создает данные для 5-раундового боя
     */
    public static Map<String, Object> createChampionshipFightData() {
        Map<String, Object> fightData = createFightData("Чемпион", "Претендент");
        fightData.put("roundsPlayed", 5);
        fightData.put("ratingPoints", 95);
        fightData.put("rankingPosition", 1);
        fightData.put("weightClass", WeightClass.HEAVYWEIGHT.name());
        fightData.put("notes", "Чемпионский бой за титул");
        return fightData;
    }

    /**
     * Логирует информацию о тесте
     */
    public static void logTestInfo(String testName, String description, String endpoint) {
        log.info("=== {} ===", testName);
        log.info("Описание: {}", description);
        log.info("Endpoint: {}", endpoint);
    }
}
