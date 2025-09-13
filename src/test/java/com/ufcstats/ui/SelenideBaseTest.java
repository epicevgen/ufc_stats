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
        
        // Проверяем системное свойство для headless режима
        String headless = System.getProperty("selenide.headless", "false");
        Configuration.headless = "true".equals(headless);
        
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = true;
        Configuration.savePageSource = true;
        Configuration.holdBrowserOpen = false; // Всегда закрываем браузер после теста
        
        // Настройка логирования (без Allure)
        Configuration.reportsFolder = "build/reports/tests";
        
        // Открываем базовую страницу
        open("http://localhost:" + port);
    }

    @AfterEach
    void tearDown() {
        // Закрываем браузер после каждого теста
        Selenide.closeWebDriver();
    }

    /**
     * Получить базовый URL приложения
     */
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
