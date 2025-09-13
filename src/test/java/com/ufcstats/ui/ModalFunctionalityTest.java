package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ModalFunctionalityTest {

    private static final Logger log = LoggerFactory.getLogger(ModalFunctionalityTest.class);

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = true;
        Configuration.savePageSource = true;
        Configuration.holdBrowserOpen = false;
        Configuration.reportsFolder = "build/reports/tests";
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void testNewFightModalOpens() {
        log.info("Тестируем открытие модального окна для создания нового боя");
        
        // Открываем главную страницу
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Нажимаем кнопку "Добавить бой"
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки модального окна
        sleep(2000);
        
        // Проверяем, что модальное окно открылось
        $("#newFightModal").shouldBe(visible);
        $("#newFightModal .modal-title").shouldHave(text("Новый бой"));
        
        // Проверяем, что форма загрузилась
        $("#newFightFormContainer form").shouldBe(visible);
        
        log.info("Модальное окно для создания нового боя успешно открылось");
    }

    @Test
    void testModalCanBeClosed() {
        log.info("Тестируем закрытие модального окна");
        
        // Открываем главную страницу
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Нажимаем кнопку "Добавить бой"
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки модального окна
        sleep(2000);
        
        // Проверяем, что модальное окно открылось
        $("#newFightModal").shouldBe(visible);
        
        // Закрываем модальное окно
        $("#newFightModal .btn-close").click();
        
        // Ждем закрытия
        sleep(1000);
        
        // Проверяем, что модальное окно закрылось
        $("#newFightModal").shouldNotBe(visible);
        
        log.info("Модальное окно успешно закрылось");
    }

    @Test
    void testBasicFightCreation() {
        log.info("Тестируем создание простого боя через модальное окно");
        
        // Открываем главную страницу
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Запоминаем количество боев до создания
        int initialFightCount = $$(".table tbody tr").size();
        log.info("Количество боев до создания: {}", initialFightCount);
        
        // Нажимаем кнопку "Добавить бой"
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки модального окна
        sleep(2000);
        
        // Проверяем, что модальное окно открылось
        $("#newFightModal").shouldBe(visible);
        $("#newFightFormContainer form").shouldBe(visible);
        
        // Заполняем основную информацию
        $("#myFighter").setValue("Тестовый боец");
        $("#opponent").setValue("Тестовый соперник");
        $("#season").setValue("2024");
        $("#roundsPlayed").setValue("3");
        
        // Выбираем опции
        $("#mode").selectOptionByValue("MMA");
        $("#result").selectOptionByValue("WIN");
        $("#method").selectOptionByValue("DECISION");
        $("#weightClass").selectOptionByValue("LIGHTWEIGHT");
        
        // Отправляем форму
        $("#newFightFormContainer form").submit();
        
        // Ждем обработки
        sleep(3000);
        
        // Проверяем, что модальное окно закрылось
        $("#newFightModal").shouldNotBe(visible);
        
        // Проверяем, что количество боев увеличилось
        int finalFightCount = $$(".table tbody tr").size();
        log.info("Количество боев после создания: {}", finalFightCount);
        
        // Проверяем, что новый бой появился в списке
        $(".table").shouldHave(text("Тестовый боец"));
        $(".table").shouldHave(text("Тестовый соперник"));
        
        log.info("Бой успешно создан через модальное окно");
    }
}
