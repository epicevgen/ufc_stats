package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Рабочий тест для создания боя
 */
@DisplayName("Рабочий тест создания боя")
public class WorkingFormTest extends SelenideBaseTest {

    private static final Logger log = LoggerFactory.getLogger(WorkingFormTest.class);

    @Test
    void testCreateFight() {
        log.info("Начинаем тест создания боя");
        
        // Переходим на страницу создания боя
        open(getBaseUrl() + "/fights/new");
        
        // Проверяем, что форма загрузилась
        $("form").shouldBe(visible);
        $("h2").shouldHave(text("Новый бой"));
        
        // Ждем загрузки
        sleep(3000);
        
        // Заполняем поля по одному с проверками
        fillField("myFighter", "Иван Петров");
        fillField("opponent", "Алексей Сидоров");
        fillField("fightDate", "2024-01-15T20:00");
        fillField("season", "2024");
        
        // Выбираем опции
        selectOption("mode", "MMA");
        selectOption("weightClass", "LIGHTWEIGHT");
        selectOption("result", "WIN");
        selectOption("method", "DECISION");
        
        // Устанавливаем количество раундов
        fillField("roundsPlayed", "1");
        sleep(2000); // Ждем обновления динамических полей
        
        // Заполняем статистику для первого раунда
        fillField("round1_my_head_damage", "10");
        fillField("round1_my_body_damage", "5");
        fillField("round1_my_leg_damage", "2");
        fillField("round1_my_knockdowns", "1");
        
        fillField("round1_opponent_head_damage", "8");
        fillField("round1_opponent_body_damage", "3");
        fillField("round1_opponent_leg_damage", "1");
        fillField("round1_opponent_knockdowns", "0");
        
        // Заполняем судейские оценки
        fillField("judge1_my_round1", "10");
        fillField("judge1_opponent_round1", "9");
        fillField("judge2_my_round1", "10");
        fillField("judge2_opponent_round1", "9");
        fillField("judge3_my_round1", "10");
        fillField("judge3_opponent_round1", "9");
        
        // Отправляем форму
        log.info("Отправляем форму");
        SelenideElement submitButton = $("button[type='submit']").shouldBe(visible, enabled);
        executeJavaScript("arguments[0].click();", submitButton);
        
        // Ждем перенаправления
        sleep(3000);
        
        // Проверяем успешное создание
        String currentUrl = com.codeborne.selenide.WebDriverRunner.getWebDriver().getCurrentUrl();
        log.info("Текущий URL после отправки формы: {}", currentUrl);
        
        if (currentUrl.contains("/fights/new")) {
            log.error("Форма не была отправлена успешно - остались на той же странице");
            // Выводим HTML для отладки
            String html = $("body").innerHtml();
            log.error("HTML страницы: {}", html);
        } else {
            log.info("Форма была отправлена успешно - перенаправлены на: {}", currentUrl);
        }
        
        log.info("Тест завершен");
    }
    
    private void fillField(String fieldId, String value) {
        log.info("Заполняем поле {} значением {}", fieldId, value);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        field.clear();
        field.setValue(value);
        sleep(500);
        
        // Проверяем, что поле заполнилось
        String actualValue = field.getValue();
        if (!value.equals(actualValue)) {
            log.warn("Поле {} не заполнилось правильно. Ожидали: '{}', получили: '{}'", fieldId, value, actualValue);
        } else {
            log.info("Поле {} заполнено успешно", fieldId);
        }
    }
    
    private void selectOption(String selectId, String value) {
        log.info("Выбираем опцию {} в поле {}", value, selectId);
        SelenideElement select = $("#" + selectId);
        select.shouldBe(visible);
        select.selectOptionByValue(value);
        sleep(500);
        log.info("Опция {} выбрана в поле {}", value, selectId);
    }
}
