package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Простой тест для проверки работы модального окна
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SimpleModalTest extends SelenideBaseTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        $("#fights-table").shouldBe(visible);
    }

    @Test
    void testSimpleFightCreation() {
        log.info("=== Начинаем простой тест создания боя ===");
        
        // Открываем модальное окно создания
        $("button[data-bs-target='#newFightModal']").shouldBe(visible).click();
        $("#newFightModal").shouldBe(visible);
        $("#newFightFormContainer form").shouldBe(visible);

        // Заполняем только обязательные поля
        fillField("myFighter", "Тест Боец");
        fillField("opponent", "Тест Соперник");
        fillDateField("fightDate", "2024-01-15T20:00");
        fillField("season", "2024");
        selectOption("weightClass", "LIGHTWEIGHT");
        selectOption("fightMode", "MMA");
        selectOption("fightResult", "WIN");
        selectOption("fightMethod", "DECISION");
        fillField("roundsPlayed", "3");

        // Отправляем форму
        $("#newFightModal button[type='submit']").click();

        // Ждем закрытия модального окна
        $("#newFightModal").shouldNotBe(visible);
        
        log.info("=== Простой тест создания боя завершен успешно ===");
    }

    private void fillField(String fieldId, String value) {
        log.debug("Заполняем поле {} значением {}", fieldId, value);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        field.setValue(value);
        sleep(500);
    }

    private void fillDateField(String fieldId, String value) {
        log.debug("Заполняем поле даты {} значением {}", fieldId, value);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        
        // Используем JavaScript для установки значения datetime-local
        executeJavaScript("document.getElementById('" + fieldId + "').value = '" + value + "';");
        sleep(500);
    }

    private void selectOption(String selectId, String value) {
        log.debug("Выбираем опцию {} в поле {}", value, selectId);
        SelenideElement select = $("#" + selectId);
        select.shouldBe(visible);
        select.selectOptionByValue(value);
        sleep(500);
    }
}

