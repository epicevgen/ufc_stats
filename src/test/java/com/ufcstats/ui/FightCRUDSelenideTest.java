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
        $("#fight-form").shouldBe(visible);
        
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
        $("#fight-form").shouldBe(visible);
        
        // Заполняем форму
        fillFightForm();
        
        // Отправляем форму
        $("button[type='submit']").click();
        
        // Ждем перенаправления
        $("h1").shouldHave(text("Список боев"));
        
        // Получаем ID созданного боя из URL или из таблицы
        String currentUrl = com.codeborne.selenide.WebDriverRunner.getWebDriver().getCurrentUrl();
        if (currentUrl.contains("/fights/")) {
            String idStr = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
            return Long.parseLong(idStr);
        }
        
        // Если перенаправило на список, берем ID из первой строки таблицы
        SelenideElement firstRow = $$("#fights-table tbody tr").first();
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
        $(".fight-details").shouldBe(visible);
        $(".fight-details").shouldHave(text("Иван Петров"));
        $(".fight-details").shouldHave(text("Алексей Сидоров"));
        
        // Проверяем статистику раундов
        $(".rounds-stats").shouldBe(visible);
        
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
        $("button[type='submit']").click();
        
        // Проверяем, что вернулись на страницу деталей
        $("h4").shouldHave(text("Бой #" + fightId));
        $(".fight-details").shouldHave(text("Обновленные заметки о бое"));
        
        log.info("Бой успешно обновлен");
    }

    private void deleteFight(Long fightId) {
        log.info("Удаляем бой с ID: {}", fightId);
        
        // Переходим на страницу деталей
        open(getBaseUrl() + "/fights/" + fightId);
        
        // Находим кнопку удаления и кликаем
        $("#delete-button").shouldBe(visible, enabled).click();
        
        // Подтверждаем удаление в диалоге
        confirm();
        
        // Проверяем, что перенаправило на список боев
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем, что бой больше не отображается в списке
        $("#fights-table").shouldNotHave(text("Иван Петров"));
        
        log.info("Бой успешно удален");
    }

    private void fillFightForm() {
        log.info("Заполняем форму боя");
        
        // Основная информация
        $("#myFighter").setValue("Иван Петров");
        $("#opponent").setValue("Алексей Сидоров");
        $("#fightDate").setValue("2024-01-15T20:00");
        $("#mode").selectOptionByValue("MMA");
        $("#weightClass").selectOptionByValue("LIGHTWEIGHT");
        $("#result").selectOptionByValue("WIN");
        $("#method").selectOptionByValue("DECISION");
        $("#notes").setValue("Тестовый бой для CRUD операций");
        
        // Устанавливаем количество раундов
        $("#roundsPlayed").setValue("3");
        sleep(1000); // Ждем обновления полей
        
        // Заполняем статистику для 3 раундов
        for (int round = 1; round <= 3; round++) {
            fillRoundStats(round);
        }
        
        // Заполняем судейские оценки
        fillJudgeScores();
    }

    private void fillRoundStats(int round) {
        log.info("Заполняем статистику для раунда {}", round);
        
        // Статистика для первого бойца
        $("#round-" + round + "-my-total-strikes-attempted").setValue("15");
        $("#round-" + round + "-my-total-strikes-landed").setValue("12");
        $("#round-" + round + "-my-significant-strikes-attempted").setValue("10");
        $("#round-" + round + "-my-significant-strikes-landed").setValue("8");
        $("#round-" + round + "-my-head-damage").setValue("5");
        $("#round-" + round + "-my-body-damage").setValue("3");
        $("#round-" + round + "-my-leg-damage").setValue("2");
        $("#round-" + round + "-my-knockdowns").setValue("0");
        $("#round-" + round + "-my-takedowns-attempted").setValue("1");
        $("#round-" + round + "-my-takedowns-successful").setValue("1");
        $("#round-" + round + "-my-control-time").setValue("45");
        
        // Статистика для второго бойца
        $("#round-" + round + "-opponent-total-strikes-attempted").setValue("12");
        $("#round-" + round + "-opponent-total-strikes-landed").setValue("9");
        $("#round-" + round + "-opponent-significant-strikes-attempted").setValue("8");
        $("#round-" + round + "-opponent-significant-strikes-landed").setValue("6");
        $("#round-" + round + "-opponent-head-damage").setValue("3");
        $("#round-" + round + "-opponent-body-damage").setValue("2");
        $("#round-" + round + "-opponent-leg-damage").setValue("1");
        $("#round-" + round + "-opponent-knockdowns").setValue("0");
        $("#round-" + round + "-opponent-takedowns-attempted").setValue("0");
        $("#round-" + round + "-opponent-takedowns-successful").setValue("0");
        $("#round-" + round + "-opponent-control-time").setValue("15");
    }

    private void fillJudgeScores() {
        log.info("Заполняем судейские оценки");
        
        $("#judge1-score-fighter1").setValue("10");
        $("#judge1-score-fighter2").setValue("9");
        $("#judge2-score-fighter1").setValue("10");
        $("#judge2-score-fighter2").setValue("9");
        $("#judge3-score-fighter1").setValue("10");
        $("#judge3-score-fighter2").setValue("9");
    }
}
