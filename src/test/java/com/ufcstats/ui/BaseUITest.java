package com.ufcstats.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

/**
 * Базовый класс для UI тестов
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public abstract class BaseUITest {

    @LocalServerPort
    protected int port;

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;

    @BeforeEach
    void setUp() {
        log.info("Настройка WebDriver для UI тестов на порту: {}", port);
        
        // Настройка Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Запуск в headless режиме для CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
        
        log.info("WebDriver настроен, базовый URL: {}", baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            log.info("Закрытие WebDriver");
            driver.quit();
        }
    }

    /**
     * Переход на указанную страницу
     */
    protected void navigateTo(String path) {
        String url = baseUrl + path;
        log.info("Переход на страницу: {}", url);
        driver.get(url);
    }

    /**
     * Ожидание загрузки страницы
     */
    protected void waitForPageLoad() {
        wait.until(webDriver -> 
            webDriver.getCurrentUrl().startsWith(baseUrl) &&
            webDriver.getTitle() != null && !webDriver.getTitle().isEmpty()
        );
    }

    /**
     * Получение текущего URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Получение заголовка страницы
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
}
