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
import static com.codeborne.selenide.CollectionCondition.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ModalFixesTest {

    private static final Logger log = LoggerFactory.getLogger(ModalFixesTest.class);

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
    void testDropdownValuesAreDifferent() {
        log.info("Тестируем, что выпадающие списки имеют разные значения");
        
        // Открываем страницу создания боя напрямую
        open(getBaseUrl() + "/fights/new");
        
        // Проверяем, что страница загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем выпадающий список "Режим боя"
        $$("#mode option").shouldHave(sizeGreaterThan(1));
        $("#mode option[value='STANCE']").shouldHave(text("Стойка"));
        $("#mode option[value='MMA']").shouldHave(text("ММА"));
        
        // Проверяем выпадающий список "Результат"
        $$("#result option").shouldHave(sizeGreaterThan(1));
        $("#result option[value='WIN']").shouldHave(text("Победа"));
        $("#result option[value='LOSS']").shouldHave(text("Поражение"));
        $("#result option[value='DRAW']").shouldHave(text("Ничья"));
        
        // Проверяем выпадающий список "Метод"
        $$("#method option").shouldHave(sizeGreaterThan(1));
        $("#method option[value='DECISION']").shouldHave(text("Решение"));
        $("#method option[value='SUBMISSION']").shouldHave(text("Сабмишен"));
        $("#method option[value='KNOCKOUT']").shouldHave(text("Нокаут"));
        $("#method option[value='EARLY_EXIT']").shouldHave(text("Досрочный выход"));
        
        log.info("Все выпадающие списки имеют корректные разные значения");
    }

    @Test
    void testRoundStatisticsFieldsExist() {
        log.info("Тестируем наличие полей статистики по раундам");
        
        // Открываем страницу создания боя напрямую
        open(getBaseUrl() + "/fights/new");
        
        // Проверяем, что страница загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем, что есть контейнер для раундов
        $("#roundsContainer").shouldBe(visible);
        
        // Проверяем, что есть контейнер для судей
        $("#judgesContainer").shouldBe(visible);
        
        // Проверяем, что есть поле для количества раундов
        $("#roundsPlayed").shouldBe(visible);
        
        // Устанавливаем количество раундов = 3
        $("#roundsPlayed").setValue("3");
        
        // Ждем обновления полей
        sleep(2000);
        
        // Проверяем, что появились поля для раундов
        $("#round1_my_head_damage").shouldBe(visible);
        $("#round2_my_head_damage").shouldBe(visible);
        $("#round3_my_head_damage").shouldBe(visible);
        
        // Проверяем, что появились поля для судей
        $("#judge1_my_round1").shouldBe(visible);
        $("#judge2_my_round1").shouldBe(visible);
        $("#judge3_my_round1").shouldBe(visible);
        
        log.info("Поля статистики по раундам и судейских оценок присутствуют");
    }

    @Test
    void testBasicFormFields() {
        log.info("Тестируем основные поля формы");
        
        // Открываем страницу создания боя напрямую
        open(getBaseUrl() + "/fights/new");
        
        // Проверяем, что страница загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем основные поля
        $("#myFighter").shouldBe(visible);
        $("#opponent").shouldBe(visible);
        $("#fightDate").shouldBe(visible);
        $("#season").shouldBe(visible);
        $("#roundsPlayed").shouldBe(visible);
        $("#mode").shouldBe(visible);
        $("#result").shouldBe(visible);
        $("#method").shouldBe(visible);
        $("#weightClass").shouldBe(visible);
        
        log.info("Все основные поля формы присутствуют");
    }
}
