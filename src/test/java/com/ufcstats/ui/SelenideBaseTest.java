package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.open;

/**
 * Базовый класс для UI тестов с использованием Selenide
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class SelenideBaseTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void setUp() {
        // Настройка Selenide
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = true;
        Configuration.savePageSource = true;
        
        // Настройка логирования (без Allure)
        Configuration.reportsFolder = "build/reports/tests";
        
        // Открываем базовую страницу
        open("http://localhost:" + port);
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    /**
     * Получить базовый URL приложения
     */
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
