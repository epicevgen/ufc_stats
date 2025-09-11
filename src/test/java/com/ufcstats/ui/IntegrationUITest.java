package com.ufcstats.ui;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные UI тесты с использованием Testcontainers
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
@DisplayName("Интеграционные UI тесты")
class IntegrationUITest extends BaseUITest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("ufc_stats_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    @DisplayName("Полный цикл создания, просмотра, редактирования и удаления боя")
    void testCompleteFightLifecycle() {
        log.info("Тест: Полный цикл работы с боем");
        
        // 1. Создание нового боя
        log.info("Шаг 1: Создание нового боя");
        navigateTo("/fights/new");
        waitForPageLoad();
        
        fillCompleteFightForm();
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // 2. Проверка отображения в списке
        log.info("Шаг 2: Проверка отображения в списке");
        WebElement fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.getText()).contains("Интеграционный тест боец");
        assertThat(fightsTable.getText()).contains("Интеграционный тест соперник");
        
        // 3. Просмотр деталей боя
        log.info("Шаг 3: Просмотр деталей боя");
        WebElement viewLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[href*='/fights/'][href*='/view']")
        ));
        viewLink.click();
        
        // Проверка отображения деталей
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fight-details")));
        WebElement fightDetails = driver.findElement(By.id("fight-details"));
        assertThat(fightDetails.getText()).contains("Интеграционный тест боец");
        assertThat(fightDetails.getText()).contains("Интеграционный тест соперник");
        
        // 4. Редактирование боя
        log.info("Шаг 4: Редактирование боя");
        WebElement editButton = driver.findElement(By.linkText("Редактировать"));
        editButton.click();
        
        // Проверка, что мы на странице редактирования
        wait.until(ExpectedConditions.urlContains("/edit"));
        
        // Изменение данных
        WebElement myFighterField = driver.findElement(By.id("myFighter"));
        myFighterField.clear();
        myFighterField.sendKeys("Обновленный интеграционный боец");
        
        // Сохранение изменений
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        saveButton.click();
        
        // Проверка перенаправления
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // 5. Проверка обновленных данных
        log.info("Шаг 5: Проверка обновленных данных");
        fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.getText()).contains("Обновленный интеграционный боец");
        
        // 6. Удаление боя
        log.info("Шаг 6: Удаление боя");
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-action='delete']")
        ));
        deleteButton.click();
        
        // Подтверждение удаления
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        // Проверка, что бой удален
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(
            fightsTable, "Обновленный интеграционный боец")));
        
        log.info("✓ Полный цикл работы с боем выполнен успешно");
    }

    @Test
    @DisplayName("Тестирование навигации между страницами")
    void testNavigationBetweenPages() {
        log.info("Тест: Навигация между страницами");
        
        // 1. Переход на главную страницу
        log.info("Шаг 1: Переход на главную страницу");
        navigateTo("/");
        waitForPageLoad();
        assertThat(getPageTitle()).contains("UFC Stats");
        
        // 2. Переход на панель управления
        log.info("Шаг 2: Переход на панель управления");
        WebElement dashboardLink = driver.findElement(By.linkText("Панель управления"));
        dashboardLink.click();
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertThat(getPageTitle()).contains("Панель управления");
        
        // 3. Переход на список боев
        log.info("Шаг 3: Переход на список боев");
        WebElement fightsLink = driver.findElement(By.linkText("Бои"));
        fightsLink.click();
        wait.until(ExpectedConditions.urlContains("/fights"));
        assertThat(getPageTitle()).contains("Список боев");
        
        // 4. Переход на страницу создания боя
        log.info("Шаг 4: Переход на страницу создания боя");
        WebElement newFightLink = driver.findElement(By.linkText("Новый бой"));
        newFightLink.click();
        wait.until(ExpectedConditions.urlContains("/fights/new"));
        assertThat(getPageTitle()).contains("Новый бой");
        
        // 5. Возврат к списку боев
        log.info("Шаг 5: Возврат к списку боев");
        WebElement cancelLink = driver.findElement(By.linkText("Отмена"));
        cancelLink.click();
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        log.info("✓ Навигация между страницами работает корректно");
    }

    @Test
    @DisplayName("Тестирование статистики и аналитики")
    void testStatisticsAndAnalytics() {
        log.info("Тест: Статистика и аналитика");
        
        // Создаем несколько тестовых боев для статистики
        createMultipleTestFights();
        
        // 1. Проверка статистики на панели управления
        log.info("Шаг 1: Проверка статистики на панели управления");
        navigateTo("/dashboard");
        waitForPageLoad();
        
        // Проверка отображения статистики
        WebElement statsSection = driver.findElement(By.id("statistics-summary"));
        assertThat(statsSection.isDisplayed()).isTrue();
        
        // Проверка наличия ключевых метрик
        assertThat(statsSection.getText()).contains("Всего боев");
        assertThat(statsSection.getText()).contains("Побед");
        assertThat(statsSection.getText()).contains("Поражений");
        assertThat(statsSection.getText()).contains("Процент побед");
        
        // 2. Переход на страницу детальной статистики
        log.info("Шаг 2: Переход на страницу детальной статистики");
        WebElement detailedStatsLink = driver.findElement(By.linkText("Подробная статистика"));
        detailedStatsLink.click();
        wait.until(ExpectedConditions.urlContains("/statistics"));
        
        // Проверка отображения детальной статистики
        WebElement detailedStatsSection = driver.findElement(By.id("detailed-statistics"));
        assertThat(detailedStatsSection.isDisplayed()).isTrue();
        
        // Проверка наличия графиков
        List<WebElement> charts = driver.findElements(By.tagName("canvas"));
        assertThat(charts.size()).isGreaterThan(0);
        
        log.info("✓ Статистика и аналитика работают корректно");
    }

    @Test
    @DisplayName("Тестирование производительности и отзывчивости")
    void testPerformanceAndResponsiveness() {
        log.info("Тест: Производительность и отзывчивость");
        
        // 1. Тест времени загрузки главной страницы
        log.info("Шаг 1: Тест времени загрузки главной страницы");
        long startTime = System.currentTimeMillis();
        navigateTo("/");
        waitForPageLoad();
        long loadTime = System.currentTimeMillis() - startTime;
        
        assertThat(loadTime).isLessThan(5000); // Менее 5 секунд
        log.info("Время загрузки главной страницы: {} мс", loadTime);
        
        // 2. Тест времени загрузки списка боев
        log.info("Шаг 2: Тест времени загрузки списка боев");
        startTime = System.currentTimeMillis();
        navigateTo("/fights");
        waitForPageLoad();
        loadTime = System.currentTimeMillis() - startTime;
        
        assertThat(loadTime).isLessThan(3000); // Менее 3 секунд
        log.info("Время загрузки списка боев: {} мс", loadTime);
        
        // 3. Тест отзывчивости формы
        log.info("Шаг 3: Тест отзывчивости формы");
        startTime = System.currentTimeMillis();
        navigateTo("/fights/new");
        waitForPageLoad();
        loadTime = System.currentTimeMillis() - startTime;
        
        assertThat(loadTime).isLessThan(2000); // Менее 2 секунд
        log.info("Время загрузки формы: {} мс", loadTime);
        
        log.info("✓ Производительность и отзывчивость соответствуют требованиям");
    }

    @Test
    @DisplayName("Тестирование обработки ошибок")
    void testErrorHandling() {
        log.info("Тест: Обработка ошибок");
        
        // 1. Тест несуществующей страницы
        log.info("Шаг 1: Тест несуществующей страницы");
        navigateTo("/nonexistent-page");
        waitForPageLoad();
        
        // Проверка отображения страницы ошибки
        WebElement errorPage = driver.findElement(By.id("error-page"));
        assertThat(errorPage.isDisplayed()).isTrue();
        assertThat(errorPage.getText()).contains("404");
        
        // 2. Тест несуществующего боя
        log.info("Шаг 2: Тест несуществующего боя");
        navigateTo("/fights/99999");
        waitForPageLoad();
        
        // Проверка отображения ошибки
        WebElement notFoundMessage = driver.findElement(By.className("not-found"));
        assertThat(notFoundMessage.isDisplayed()).isTrue();
        assertThat(notFoundMessage.getText()).contains("Бой не найден");
        
        log.info("✓ Обработка ошибок работает корректно");
    }

    /**
     * Заполнение полной формы боя
     */
    private void fillCompleteFightForm() {
        // Основные поля
        driver.findElement(By.id("myFighter")).sendKeys("Интеграционный тест боец");
        driver.findElement(By.id("opponent")).sendKeys("Интеграционный тест соперник");
        
        // Выбор режима, результата и метода
        driver.findElement(By.id("mode")).sendKeys("ММА");
        driver.findElement(By.id("result")).sendKeys("Победа");
        driver.findElement(By.id("method")).sendKeys("Нокаут");
        
        // Дополнительные поля
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("200");
        driver.findElement(By.id("rankingPosition")).sendKeys("1");
        driver.findElement(By.id("notes")).sendKeys("Интеграционный тест бой");
        
        // Заполнение статистики раундов
        fillRoundStatistics();
        
        // Заполнение судейских оценок
        fillJudgeScores();
    }

    /**
     * Заполнение статистики раундов
     */
    private void fillRoundStatistics() {
        // Статистика первого раунда
        driver.findElement(By.id("round-1-my-significant-strikes-landed")).sendKeys("20");
        driver.findElement(By.id("round-1-my-significant-strikes-attempted")).sendKeys("30");
        driver.findElement(By.id("round-1-my-takedowns-successful")).sendKeys("3");
        driver.findElement(By.id("round-1-my-takedowns-attempted")).sendKeys("4");
        driver.findElement(By.id("round-1-my-control-time")).sendKeys("02:00");
        
        driver.findElement(By.id("round-1-opponent-significant-strikes-landed")).sendKeys("15");
        driver.findElement(By.id("round-1-opponent-significant-strikes-attempted")).sendKeys("25");
        driver.findElement(By.id("round-1-opponent-takedowns-successful")).sendKeys("1");
        driver.findElement(By.id("round-1-opponent-takedowns-attempted")).sendKeys("2");
        driver.findElement(By.id("round-1-opponent-control-time")).sendKeys("01:00");
    }

    /**
     * Заполнение судейских оценок
     */
    private void fillJudgeScores() {
        // Оценки первого судьи
        driver.findElement(By.id("judge-1-round-1-my-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-1-opponent-score")).sendKeys("9");
        driver.findElement(By.id("judge-1-round-2-my-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-2-opponent-score")).sendKeys("9");
        driver.findElement(By.id("judge-1-round-3-my-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-3-opponent-score")).sendKeys("9");
    }

    /**
     * Создание нескольких тестовых боев
     */
    private void createMultipleTestFights() {
        log.info("Создание нескольких тестовых боев для статистики");
        
        String[] fighters = {"Боец 1", "Боец 2", "Боец 3"};
        String[] opponents = {"Соперник 1", "Соперник 2", "Соперник 3"};
        
        for (int i = 0; i < fighters.length; i++) {
            navigateTo("/fights/new");
            waitForPageLoad();
            
            driver.findElement(By.id("myFighter")).sendKeys(fighters[i]);
            driver.findElement(By.id("opponent")).sendKeys(opponents[i]);
            driver.findElement(By.id("mode")).sendKeys("ММА");
            driver.findElement(By.id("result")).sendKeys("Победа");
            driver.findElement(By.id("method")).sendKeys("Нокаут");
            driver.findElement(By.id("season")).sendKeys("1");
            driver.findElement(By.id("ratingPoints")).sendKeys("100");
            driver.findElement(By.id("rankingPosition")).sendKeys(String.valueOf(i + 1));
            
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();
            
            wait.until(ExpectedConditions.urlContains("/fights"));
        }
    }
}
