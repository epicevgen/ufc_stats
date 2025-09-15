package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
public class SimpleModalTest {

    private static final Logger log = LoggerFactory.getLogger(SimpleModalTest.class);

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeAll
    static void setup() {
        Configuration.headless = false;
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }

    @Test
    @DisplayName("Проверка открытия модального окна создания боя")
    void testOpenCreateFightModal() {
        log.info("Тест: Проверка открытия модального окна создания боя");
        
        // Открываем страницу списка боев
        open(getBaseUrl() + "/fights");
        
        // Проверяем, что страница загрузилась
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем наличие таблицы боев
        $("#fights-table").shouldBe(visible);
        
        // Кликаем по кнопке "Добавить бой"
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем, пока модальное окно откроется
        $("#newFightModal").shouldBe(visible);
        
        // Проверяем, что форма загрузилась
        $("#newFightFormContainer").shouldBe(visible);
        
        log.info("✓ Модальное окно успешно открылось");
    }
}
