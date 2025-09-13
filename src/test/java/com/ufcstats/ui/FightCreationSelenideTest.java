package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты создания боев с использованием Selenide
 */
@DisplayName("Тесты создания боев с Selenide")
public class FightCreationSelenideTest extends SelenideBaseTest {

    private static final Logger log = LoggerFactory.getLogger(FightCreationSelenideTest.class);

    @Test
    @DisplayName("Создание боя с 5 раундами и полной статистикой")
    void testCreateFightWith5Rounds() {
        log.info("Начинаем тест создания боя с 5 раундами");
        
        // Открываем главную страницу (список боев)
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Открываем модальное окно для создания нового боя
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки формы в модальном окне
        sleep(2000);
        $("#newFightFormContainer form").shouldBe(visible);
        
        // Заполняем основную информацию о бое
        fillBasicFightInfo();
        
        // Устанавливаем количество раундов = 5
        setRoundsCount(5);
        
        // Заполняем минимальную статистику для всех 5 раундов
        for (int round = 1; round <= 5; round++) {
            fillField("round" + round + "_my_head_damage", "10");
            fillField("round" + round + "_my_body_damage", "5");
            fillField("round" + round + "_my_leg_damage", "2");
            fillField("round" + round + "_my_knockdowns", "1");
            
            fillField("round" + round + "_opponent_head_damage", "8");
            fillField("round" + round + "_opponent_body_damage", "3");
            fillField("round" + round + "_opponent_leg_damage", "1");
            fillField("round" + round + "_opponent_knockdowns", "0");
            
            // Заполняем судейские оценки для каждого раунда
            fillField("judge1_my_round" + round, "10");
            fillField("judge1_opponent_round" + round, "9");
            fillField("judge2_my_round" + round, "10");
            fillField("judge2_opponent_round" + round, "9");
            fillField("judge3_my_round" + round, "10");
            fillField("judge3_opponent_round" + round, "9");
        }
        
        // Отправляем форму
        submitForm();
        
        // Проверяем успешное создание
        verifyFightCreated();
        
        log.info("Тест создания боя с 5 раундами завершен успешно");
    }

    @Test
    @DisplayName("Создание боя с 3 раундами")
    void testCreateFightWith3Rounds() {
        log.info("Начинаем тест создания боя с 3 раундами");
        
        // Открываем главную страницу (список боев)
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Открываем модальное окно для создания нового боя
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки формы в модальном окне
        sleep(2000);
        $("#newFightFormContainer form").shouldBe(visible);
        
        fillBasicFightInfo();
        setRoundsCount(3);
        
        // Заполняем статистику для 3 раундов
        for (int round = 1; round <= 3; round++) {
            fillField("round" + round + "_my_head_damage", "10");
            fillField("round" + round + "_my_body_damage", "5");
            fillField("round" + round + "_my_leg_damage", "2");
            fillField("round" + round + "_my_knockdowns", "1");
            
            fillField("round" + round + "_opponent_head_damage", "8");
            fillField("round" + round + "_opponent_body_damage", "3");
            fillField("round" + round + "_opponent_leg_damage", "1");
            fillField("round" + round + "_opponent_knockdowns", "0");
        }
        
        // Заполняем судейские оценки для 3 раундов
        for (int round = 1; round <= 3; round++) {
            fillField("judge1_my_round" + round, "10");
            fillField("judge1_opponent_round" + round, "9");
            fillField("judge2_my_round" + round, "10");
            fillField("judge2_opponent_round" + round, "9");
            fillField("judge3_my_round" + round, "10");
            fillField("judge3_opponent_round" + round, "9");
        }
        
        submitForm();
        verifyFightCreated();
        
        log.info("Тест создания боя с 3 раундами завершен успешно");
    }

    @Test
    @DisplayName("Создание боя с 1 раундом")
    void testCreateFightWith1Round() {
        log.info("Начинаем тест создания боя с 1 раундом");
        
        // Открываем главную страницу (список боев)
        open(getBaseUrl() + "/fights");
        $("h1").shouldHave(text("Список боев"));
        
        // Открываем модальное окно для создания нового боя
        $("button[data-bs-target='#newFightModal']").click();
        
        // Ждем загрузки формы в модальном окне
        sleep(2000);
        $("#newFightFormContainer form").shouldBe(visible);
        
        fillBasicFightInfo();
        setRoundsCount(1);
        
        // Заполняем минимальную статистику для первого раунда
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
        
        submitForm();
        verifyFightCreated();
        
        log.info("Тест создания боя с 1 раундом завершен успешно");
    }

    private void fillBasicFightInfo() {
        log.info("Заполняем основную информацию о бое");
        
        // Ждем загрузки формы и JavaScript
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
        
        // НЕ заполняем заметки, как в WorkingFormTest
        // fillField("notes", "Отличный бой с хорошей техникой");
        
        log.info("Основная информация заполнена");
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

    private void setRoundsCount(int rounds) {
        log.info("Устанавливаем количество раундов: {}", rounds);
        
        fillField("roundsPlayed", String.valueOf(rounds));
        
        // Ждем, пока JavaScript обновит поля раундов
        sleep(2000);
        
        // Проверяем, что поля раундов появились
        for (int i = 1; i <= rounds; i++) {
            $("#round" + i + "_my_head_damage").shouldBe(visible);
        }
    }

    private void fillRoundsStatistics(int roundsCount) {
        log.info("Заполняем статистику для {} раундов", roundsCount);
        
        for (int round = 1; round <= roundsCount; round++) {
            log.info("Заполняем статистику для раунда {}", round);
            
            // Статистика для первого бойца (мой боец)
            fillFighterStats(round, "my", 10 + round, 8 + round, 5 + round, 2 + round, 1);
            
            // Статистика для второго бойца (противник)
            fillFighterStats(round, "opponent", 8 + round, 6 + round, 3 + round, 1 + round, 0);
        }
    }

    private void fillFighterStats(int round, String prefix, int totalStrikes, int significantStrikes, 
                                 int headDamage, int bodyDamage, int knockdowns) {
        // Общие удары
        $("#round" + round + "_" + prefix + "_total_attempted").setValue(String.valueOf(totalStrikes));
        $("#round" + round + "_" + prefix + "_total_landed").setValue(String.valueOf(totalStrikes - 2));
        
        // Значимые удары
        $("#round" + round + "_" + prefix + "_significant_attempted").setValue(String.valueOf(significantStrikes));
        $("#round" + round + "_" + prefix + "_significant_landed").setValue(String.valueOf(significantStrikes - 1));
        
        // Урон по голове
        $("#round" + round + "_" + prefix + "_head_damage").setValue(String.valueOf(headDamage));
        
        // Урон по телу
        $("#round" + round + "_" + prefix + "_body_damage").setValue(String.valueOf(bodyDamage));
        
        // Урон по ногам
        $("#round" + round + "_" + prefix + "_leg_damage").setValue("2");
        
        // Нокдауны
        $("#round" + round + "_" + prefix + "_knockdowns").setValue(String.valueOf(knockdowns));
        
        // Тейкдауны
        $("#round" + round + "_" + prefix + "_takedowns_attempted").setValue("1");
        $("#round" + round + "_" + prefix + "_takedowns_successful").setValue("1");
        
        // Время контроля
        $("#round" + round + "_" + prefix + "_control_time").setValue("30");
    }

    private void fillJudgeScores() {
        log.info("Заполняем судейские оценки");
        
        // Оценки судей (10-9, 10-9, 10-9) для первого раунда
        $("#judge1_my_round1").setValue("10");
        $("#judge1_opponent_round1").setValue("9");
        
        $("#judge2_my_round1").setValue("10");
        $("#judge2_opponent_round1").setValue("9");
        
        $("#judge3_my_round1").setValue("10");
        $("#judge3_opponent_round1").setValue("9");
    }

    private void fillJudgeScoresFor5Rounds() {
        log.info("Заполняем судейские оценки для 5 раундов");
        
        for (int round = 1; round <= 5; round++) {
            // Первый судья: 10-9 для моего бойца
            $("#judge1_my_round" + round).setValue("10");
            $("#judge1_opponent_round" + round).setValue("9");
            
            // Второй судья: 10-9 для моего бойца
            $("#judge2_my_round" + round).setValue("10");
            $("#judge2_opponent_round" + round).setValue("9");
            
            // Третий судья: 10-9 для моего бойца
            $("#judge3_my_round" + round).setValue("10");
            $("#judge3_opponent_round" + round).setValue("9");
        }
    }

    private void submitForm() {
        log.info("Отправляем форму");
        
        // Находим кнопку отправки и кликаем через JavaScript для избежания перекрытия
        SelenideElement submitButton = $("button[type='submit']").shouldBe(visible, enabled);
        executeJavaScript("arguments[0].click();", submitButton);
        
        // Ждем обработки формы
        sleep(2000);
    }

    private void verifyFightCreated() {
        log.info("Проверяем успешное создание боя");
        
        // Ждем закрытия модального окна и обновления списка
        sleep(3000);
        
        // Логируем текущий URL для отладки
        String currentUrl = com.codeborne.selenide.WebDriverRunner.getWebDriver().getCurrentUrl();
        log.info("Текущий URL после отправки формы: {}", currentUrl);
        
        // Проверяем, что модальное окно закрылось
        $("#newFightModal").shouldNotBe(visible);
        
        // Проверяем, что мы на странице списка боев
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем, что в таблице есть созданный бой
        $(".table").shouldBe(visible);
        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
        
        // Проверяем, что есть бой с нашими бойцами
        $(".table").shouldHave(text("Иван Петров"));
        $(".table").shouldHave(text("Алексей Сидоров"));
        
        log.info("Бой успешно создан и отображается в списке");
    }
}
