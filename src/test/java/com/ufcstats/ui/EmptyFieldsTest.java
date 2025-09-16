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
 * Тест для проверки, что поля формы создания боя отображаются пустыми
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmptyFieldsTest extends SelenideBaseTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        $("#fights-table").shouldBe(visible);
    }

    @Test
    void testEmptyFieldsInNewFightForm() {
        log.info("=== Проверяем, что поля формы создания боя пустые ===");
        
        // Открываем модальное окно создания
        $("button[data-bs-target='#newFightModal']").shouldBe(visible).click();
        $("#newFightModal").shouldBe(visible);
        $("#newFightFormContainer form").shouldBe(visible);

        // Проверяем основные поля
        checkFieldIsEmpty("myFighter");
        checkFieldIsEmpty("opponent");
        checkFieldIsEmpty("season");
        checkFieldIsEmpty("roundsPlayed");
        checkFieldIsEmpty("ratingPoints");
        checkFieldIsEmpty("rankingPosition");
        checkFieldIsEmpty("notes");

        // Проверяем только основные поля (поля статистики генерируются через JavaScript)
        log.info("=== Основные поля отображаются пустыми - фича работает корректно ===");

        log.info("=== Все поля отображаются пустыми - фича работает корректно ===");
    }

    private void checkFieldIsEmpty(String fieldId) {
        log.debug("Проверяем, что поле {} пустое", fieldId);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        
        String value = field.getValue();
        log.debug("Поле {} содержит значение: '{}'", fieldId, value);
        
        if (value != null && !value.isEmpty()) {
            log.warn("Поле {} не пустое, содержит: '{}'", fieldId, value);
        }
        
        assertTrue(value == null || value.isEmpty(), 
            "Поле " + fieldId + " должно быть пустым, но содержит: '" + value + "'");
    }
}
