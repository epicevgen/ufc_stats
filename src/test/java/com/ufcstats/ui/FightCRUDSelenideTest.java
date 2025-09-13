package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты CRUD операций с боями с использованием Selenide
 */
@DisplayName("Тесты CRUD операций с боями (Selenide)")
public class FightCRUDSelenideTest extends SelenideBaseTest {

    private static final Logger log = LoggerFactory.getLogger(FightCRUDSelenideTest.class);

    @Test
    @DisplayName("Полный цикл CRUD операций с боем")
    void testFullFightCRUD() {
        log.info("Начинаем полный тест CRUD операций");
        
        // CREATE - Создание боя
        Long fightId = createNewFight();
        
        // READ - Просмотр созданного боя
        viewFightDetails(fightId);
        
        // UPDATE - Редактирование боя
        updateFight(fightId);
        
        // DELETE - Удаление боя
        deleteFight(fightId);
        
        log.info("Полный тест CRUD операций завершен успешно");
    }

    @Test
    @DisplayName("Создание боя с валидацией")
    void testCreateFightWithValidation() {
        log.info("Тестируем создание боя с валидацией");
        
        open(getBaseUrl() + "/fights/new");
        $("form").shouldBe(visible);
        
        // Пытаемся отправить пустую форму
        $("button[type='submit']").click();
        
        // Проверяем, что остались на той же странице (валидация сработала)
        $("h1").shouldHave(text("Создание нового боя"));
        
        // Проверяем наличие сообщений об ошибках
        $(".invalid-feedback").shouldBe(visible);
        
        log.info("Валидация работает корректно");
    }

    private Long createNewFight() {
        log.info("Создаем новый бой");
        
        open(getBaseUrl() + "/fights/new");
        $("form").shouldBe(visible);
        
        // Заполняем форму
        fillFightForm();
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем перенаправления
        $("h1").shouldHave(text("Список боев"));
        
        // Получаем ID созданного боя из URL или из таблицы
        String currentUrl = com.codeborne.selenide.WebDriverRunner.getWebDriver().getCurrentUrl();
        if (currentUrl.contains("/fights/")) {
            String idStr = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
            return Long.parseLong(idStr);
        }
        
        // Если перенаправило на список, берем ID из первой строки таблицы
        SelenideElement firstRow = $$(".table tbody tr").first();
        String href = firstRow.$("a").getAttribute("href");
        String idStr = href.substring(href.lastIndexOf("/") + 1);
        return Long.parseLong(idStr);
    }

    private void viewFightDetails(Long fightId) {
        log.info("Просматриваем детали боя с ID: {}", fightId);
        
        open(getBaseUrl() + "/fights/" + fightId);
        
        // Проверяем, что страница деталей загрузилась
        $("h4").shouldHave(text("Бой #" + fightId));
        
        // Проверяем основную информацию
        $(".card-body").shouldBe(visible);
        $(".card-body").shouldHave(text("Иван Петров"));
        $(".card-body").shouldHave(text("Алексей Сидоров"));
        
        log.info("Детали боя отображаются корректно");
    }

    private void updateFight(Long fightId) {
        log.info("Редактируем бой с ID: {}", fightId);
        
        // Переходим на страницу редактирования
        open(getBaseUrl() + "/fights/" + fightId + "/edit");
        
        // Проверяем, что форма редактирования загрузилась
        $("h2").shouldHave(text("Редактирование боя"));
        $("form").shouldBe(visible);
        
        // Изменяем заметки
        $("#notes").clear();
        $("#notes").setValue("Обновленные заметки о бое");
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Проверяем, что вернулись на страницу деталей
        $("h4").shouldHave(text("Бой #" + fightId));
        $(".card-body").shouldHave(text("Обновленные заметки о бое"));
        
        log.info("Бой успешно обновлен");
    }

    private void deleteFight(Long fightId) {
        log.info("Удаляем бой с ID: {}", fightId);
        
        // Переходим на страницу деталей
        open(getBaseUrl() + "/fights/" + fightId);
        
        // Находим кнопку удаления и кликаем
        executeJavaScript("arguments[0].click();", $("button[onclick='deleteFight()']").shouldBe(visible, enabled));
        
        // Подтверждаем удаление в диалоге
        confirm();
        
        // Проверяем, что перенаправило на список боев
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем, что бой больше не отображается в списке
        // Проверяем, что нет ссылки на удаленный бой
        $(".table").shouldNotHave(text("Бой #" + fightId));
        
        log.info("Бой успешно удален");
    }

    private void fillFightForm() {
        log.info("Заполняем форму боя");
        
        // Основная информация с использованием вспомогательных методов
        fillField("myFighter", "Иван Петров");
        fillField("opponent", "Алексей Сидоров");
        fillField("fightDate", "2024-01-15T20:00");
        selectOption("mode", "MMA");
        selectOption("weightClass", "LIGHTWEIGHT");
        selectOption("result", "WIN");
        selectOption("method", "DECISION");
        fillField("notes", "Тестовый бой для CRUD операций");
        fillField("season", "1");
        
        // Устанавливаем количество раундов
        fillField("roundsPlayed", "3");
        sleep(1000); // Ждем обновления полей
        
        // Заполняем статистику для 3 раундов
        for (int round = 1; round <= 3; round++) {
            fillRoundStats(round);
        }
        
        // Заполняем судейские оценки
        fillJudgeScores();
    }
    
    private void fillField(String fieldId, String value) {
        $("#" + fieldId).shouldBe(visible);
        $("#" + fieldId).clear();
        $("#" + fieldId).setValue(value);
        sleep(500);
    }
    
    private void selectOption(String selectId, String value) {
        $("#" + selectId).shouldBe(visible).selectOptionByValue(value);
        sleep(500);
    }

    private void fillRoundStats(int round) {
        log.info("Заполняем статистику для раунда {}", round);
        
        // Статистика для первого бойца (используем правильные ID из HTML)
        $("#round" + round + "_my_head_damage").setValue("5");
        $("#round" + round + "_my_body_damage").setValue("3");
        $("#round" + round + "_my_leg_damage").setValue("2");
        $("#round" + round + "_my_knockdowns").setValue("0");
        $("#round" + round + "_my_significant_landed").setValue("8");
        $("#round" + round + "_my_significant_attempted").setValue("10");
        
        // Статистика для второго бойца
        $("#round" + round + "_opponent_head_damage").setValue("3");
        $("#round" + round + "_opponent_body_damage").setValue("2");
        $("#round" + round + "_opponent_leg_damage").setValue("1");
        $("#round" + round + "_opponent_knockdowns").setValue("0");
        $("#round" + round + "_opponent_significant_landed").setValue("6");
        $("#round" + round + "_opponent_significant_attempted").setValue("8");
    }

    private void fillJudgeScores() {
        log.info("Заполняем судейские оценки");
        
        // Судья 1 - 3 раунда
        $("#judge1_my_round1").setValue("10");
        $("#judge1_opponent_round1").setValue("9");
        $("#judge1_my_round2").setValue("10");
        $("#judge1_opponent_round2").setValue("9");
        $("#judge1_my_round3").setValue("10");
        $("#judge1_opponent_round3").setValue("9");
        
        // Судья 2 - 3 раунда
        $("#judge2_my_round1").setValue("10");
        $("#judge2_opponent_round1").setValue("9");
        $("#judge2_my_round2").setValue("10");
        $("#judge2_opponent_round2").setValue("9");
        $("#judge2_my_round3").setValue("10");
        $("#judge2_opponent_round3").setValue("9");
        
        // Судья 3 - 3 раунда
        $("#judge3_my_round1").setValue("10");
        $("#judge3_opponent_round1").setValue("9");
        $("#judge3_my_round2").setValue("10");
        $("#judge3_opponent_round2").setValue("9");
        $("#judge3_my_round3").setValue("10");
        $("#judge3_opponent_round3").setValue("9");
    }
}
