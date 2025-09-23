package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DataLoadingTest extends SelenideBaseTest {

    private static final Logger log = LoggerFactory.getLogger(DataLoadingTest.class);

    @Test
    public void testDataLoadingInEditForm() {
        log.info("=== Тестируем загрузку данных в форме редактирования ===");
        
        // 1. Создаем бой с данными
        log.info("1. Создаем бой с данными");
        open("/fights");
        $("button[data-bs-target='#newFightModal']").click();
        $("#newFightFormContainer form").shouldBe(visible);
        
        // Заполняем основные поля
        $("#myFighter").setValue("Тест Боец");
        $("#opponent").setValue("Тест Соперник");
        $("#fightDate").setValue("2024-01-15T20:00");
        $("#season").setValue("2024");
        $("#roundsPlayed").setValue("1");
        
        // Выбираем опции
        selectOption("fightMode", "MMA");
        selectOption("fightResult", "WIN");
        selectOption("fightMethod", "DECISION");
        selectOption("weightClass", "LIGHTWEIGHT");
        
        // Ждем генерации раундов
        sleep(1000);
        
        // Заполняем статистику по раундам
        $("#round1_my_head_damage").setValue("10");
        $("#round1_my_body_damage").setValue("5");
        $("#round1_my_leg_damage").setValue("3");
        $("#round1_my_knockdowns").setValue("1");
        $("#round1_my_significant_landed").setValue("25");
        $("#round1_my_significant_attempted").setValue("40");
        $("#round1_my_total_landed").setValue("35");
        $("#round1_my_total_attempted").setValue("50");
        $("#round1_my_takedowns_successful").setValue("2");
        $("#round1_my_takedowns_attempted").setValue("3");
        $("#round1_my_control_time").setValue("02:00");
        
        // Заполняем статистику соперника
        $("#round1_opponent_head_damage").setValue("8");
        $("#round1_opponent_body_damage").setValue("4");
        $("#round1_opponent_leg_damage").setValue("2");
        $("#round1_opponent_knockdowns").setValue("0");
        $("#round1_opponent_significant_landed").setValue("20");
        $("#round1_opponent_significant_attempted").setValue("35");
        $("#round1_opponent_total_landed").setValue("30");
        $("#round1_opponent_total_attempted").setValue("45");
        $("#round1_opponent_takedowns_successful").setValue("1");
        $("#round1_opponent_takedowns_attempted").setValue("2");
        $("#round1_opponent_control_time").setValue("01:30");
        
        // Заполняем судейские оценки
        $("#judge1_my_round1").setValue("10");
        $("#judge1_opponent_round1").setValue("9");
        
        // Сохраняем
        $("#saveNewFightBtn").click();
        sleep(3000);
        
        // 2. Открываем форму редактирования
        log.info("2. Открываем форму редактирования");
        $$("button[onclick*='editFight']").first().click();
        sleep(2000);
        
        // 3. Проверяем загрузку данных
        log.info("3. Проверяем загрузку данных");
        
        // Основные поля
        verifyFieldValue("myFighter", "Тест Боец");
        verifyFieldValue("opponent", "Тест Соперник");
        verifyFieldValue("season", "2024");
        
        // Статистика по раундам
        verifyFieldValue("round1_my_head_damage", "10");
        verifyFieldValue("round1_my_body_damage", "5");
        verifyFieldValue("round1_my_leg_damage", "3");
        verifyFieldValue("round1_my_knockdowns", "1");
        verifyFieldValue("round1_my_significant_landed", "25");
        verifyFieldValue("round1_my_significant_attempted", "40");
        verifyFieldValue("round1_my_total_landed", "35");
        verifyFieldValue("round1_my_total_attempted", "50");
        verifyFieldValue("round1_my_takedowns_successful", "2");
        verifyFieldValue("round1_my_takedowns_attempted", "3");
        verifyFieldValue("round1_my_control_time", "02:00");
        
        // Статистика соперника
        verifyFieldValue("round1_opponent_head_damage", "8");
        verifyFieldValue("round1_opponent_body_damage", "4");
        verifyFieldValue("round1_opponent_leg_damage", "2");
        verifyFieldValue("round1_opponent_knockdowns", "0");
        verifyFieldValue("round1_opponent_significant_landed", "20");
        verifyFieldValue("round1_opponent_significant_attempted", "35");
        verifyFieldValue("round1_opponent_total_landed", "30");
        verifyFieldValue("round1_opponent_total_attempted", "45");
        verifyFieldValue("round1_opponent_takedowns_successful", "1");
        verifyFieldValue("round1_opponent_takedowns_attempted", "2");
        verifyFieldValue("round1_opponent_control_time", "01:30");
        
        // Судейские оценки
        verifyFieldValue("judge1_my_round1", "10");
        verifyFieldValue("judge1_opponent_round1", "9");
        
        log.info("✅ Все данные успешно загружены в форме редактирования!");
    }
    
    private void verifyFieldValue(String fieldId, String expectedValue) {
        log.debug("Проверяем поле {} на значение {}", fieldId, expectedValue);
        SelenideElement field = $("#" + fieldId);
        field.shouldBe(visible);
        String actualValue = field.getValue();
        assertEquals(expectedValue, actualValue, 
            String.format("Поле %s должно содержать значение '%s', но содержит '%s'", fieldId, expectedValue, actualValue));
        log.debug("✅ Поле {} содержит ожидаемое значение: {}", fieldId, actualValue);
    }
    
    private void selectOption(String fieldId, String value) {
        log.debug("Выбираем опцию {} в поле {}", value, fieldId);
        $("#" + fieldId).selectOption(value);
    }
}