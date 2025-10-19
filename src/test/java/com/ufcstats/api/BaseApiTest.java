package com.ufcstats.api;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

/**
 * Базовый класс для API тестов с использованием REST Assured
 * 
 * Предоставляет:
 * - Настройку REST Assured
 * - Базовые методы для HTTP запросов
 * - Логирование запросов и ответов
 * - Общие настройки для всех API тестов
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseApiTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void setUpApiTest() {
        // Настройка базового URL
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "";

        // Настройка логирования
        RestAssured.config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));

        // Добавление фильтров для логирования
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        log.info("API тест настроен для порта: {}", port);
    }

    /**
     * Создает базовую спецификацию запроса с общими настройками
     */
    protected RequestSpecification givenRequest() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    /**
     * Создает спецификацию для GET запросов
     */
    protected RequestSpecification givenGetRequest() {
        return givenRequest();
    }

    /**
     * Создает спецификацию для POST запросов
     */
    protected RequestSpecification givenPostRequest() {
        return givenRequest();
    }

    /**
     * Создает спецификацию для PUT запросов
     */
    protected RequestSpecification givenPutRequest() {
        return givenRequest();
    }

    /**
     * Создает спецификацию для DELETE запросов
     */
    protected RequestSpecification givenDeleteRequest() {
        return givenRequest();
    }

    /**
     * Получает полный URL для указанного пути
     */
    protected String getFullUrl(String path) {
        return String.format("http://localhost:%d%s", port, path);
    }

    /**
     * Логирует информацию о тесте
     */
    protected void logTestInfo(String testName, String description) {
        log.info("=== {} ===", testName);
        log.info("Описание: {}", description);
        log.info("URL: {}", getFullUrl(""));
    }
}
