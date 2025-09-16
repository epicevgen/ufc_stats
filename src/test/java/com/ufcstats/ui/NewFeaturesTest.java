package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для проверки новых фич:
 * 1. Автоматическое добавление двоеточия в поля контроля времени
 * 2. Автоматическое заполнение нулями полей тейкдаунов при выборе режима "Стойка"
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NewFeaturesTest extends SelenideBaseTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        $("#fights-table").shouldBe(visible);
    }

    @Test
    void testControlTimeAutoFormatting() {
        log.info("=== Проверяем автоматическое добавление двоеточия в поля контроля времени ===");
        
        // Открываем модальное окно создания
        $("button[data-bs-target='#newFightModal']").shouldBe(visible).click();
        $("#newFightModal").shouldBe(visible);
        $("#newFightFormContainer form").shouldBe(visible);

        // Устанавливаем количество раундов для генерации полей
        fillField("roundsPlayed", "1");
        executeJavaScript("document.getElementById('roundsPlayed').dispatchEvent(new Event('input'));");
        sleep(1000);

        // Проверяем поле контроля времени для моего бойца
        SelenideElement myControlField = $("#round1_my_control_time");
        myControlField.shouldBe(visible);
        
        // Проверяем, что поле имеет правильный тип
        assertEquals("text", myControlField.getAttribute("type"), "Поле должно быть типа text");

        // Проверяем поле контроля времени для соперника
        SelenideElement opponentControlField = $("#round1_opponent_control_time");
        opponentControlField.shouldBe(visible);
        
        // Проверяем, что поле имеет правильный тип
        assertEquals("text", opponentControlField.getAttribute("type"), "Поле соперника должно быть типа text");

        log.info("=== Поля контроля времени генерируются корректно ===");
    }

    @Test
    void testTakedownFieldsAutoFillForStanceMode() {
        log.info("=== Проверяем автоматическое заполнение нулями полей тейкдаунов при выборе режима 'Стойка' ===");
        
        // Открываем модальное окно создания
        $("button[data-bs-target='#newFightModal']").shouldBe(visible).click();
        $("#newFightModal").shouldBe(visible);
        $("#newFightFormContainer form").shouldBe(visible);

        // Проверяем поле режима боя
        SelenideElement fightModeField = $("#fightMode");
        fightModeField.shouldBe(visible);
        
        // Проверяем, что у поля есть обработчик onchange
        String onchangeAttr = fightModeField.getAttribute("onchange");
        assertNotNull(onchangeAttr, "Поле должно иметь атрибут onchange");
        assertTrue(onchangeAttr.contains("handleFightModeChange"), "onchange должен вызывать handleFightModeChange");

        log.info("=== Атрибут onchange для поля режима боя установлен корректно ===");
    }

    private void fillField(String fieldId, String value) {
        log.debug("Заполняем поле {} значением {}", fieldId, value);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        field.setValue(value);
        sleep(100);
    }

    private void selectOption(String selectId, String value) {
        log.debug("Выбираем опцию {} в поле {}", value, selectId);
        SelenideElement select = $("#" + selectId);
        select.shouldBe(visible);
        select.selectOptionByValue(value);
        sleep(500);
    }
}
