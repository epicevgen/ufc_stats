package com.ufcstats.ui;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI тесты для страницы детального просмотра боя
 */
@Slf4j
@DisplayName("UI тесты страницы деталей боя")
class FightDetailsUITest extends BaseUITest {

    @Test
    @DisplayName("Отображение основной информации о бое")
    void testFightBasicInfoDisplay() {
        log.info("Тест: Отображение основной информации о бое");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка заголовка страницы
        WebElement title = driver.findElement(By.tagName("h1"));
        assertThat(title.getText()).contains("Детали боя");
        
        // Проверка отображения основной информации
        WebElement fightInfo = driver.findElement(By.id("fight-basic-info"));
        assertThat(fightInfo.isDisplayed()).isTrue();
        
        // Проверка наличия ключевых данных
        assertThat(fightInfo.getText()).contains("Тестовый боец");
        assertThat(fightInfo.getText()).contains("Тестовый соперник");
        assertThat(fightInfo.getText()).contains("ММА");
        assertThat(fightInfo.getText()).contains("Победа");
        assertThat(fightInfo.getText()).contains("Нокаут");
        
        log.info("✓ Основная информация о бое отображается корректно");
    }

    @Test
    @DisplayName("Отображение статистики раундов")
    void testRoundStatisticsDisplay() {
        log.info("Тест: Отображение статистики раундов");
        
        // Создаем тестовый бой с детальной статистикой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия секции статистики раундов
        WebElement roundsSection = driver.findElement(By.id("rounds-statistics"));
        assertThat(roundsSection.isDisplayed()).isTrue();
        
        // Проверка отображения всех раундов
        List<WebElement> roundCards = driver.findElements(By.className("round-card"));
        assertThat(roundCards.size()).isGreaterThan(0);
        
        // Проверка содержимого первого раунда
        WebElement firstRound = roundCards.get(0);
        assertThat(firstRound.getText()).contains("Раунд 1");
        
        // Проверка наличия статистики
        assertThat(firstRound.getText()).contains("Значимые удары");
        assertThat(firstRound.getText()).contains("Тейкдауны");
        assertThat(firstRound.getText()).contains("Контроль времени");
        
        log.info("✓ Статистика раундов отображается корректно");
    }

    @Test
    @DisplayName("Отображение судейских оценок")
    void testJudgeScoresDisplay() {
        log.info("Тест: Отображение судейских оценок");
        
        // Создаем тестовый бой с судейскими оценками
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия секции судейских оценок
        WebElement judgeScoresSection = driver.findElement(By.id("judge-scores"));
        assertThat(judgeScoresSection.isDisplayed()).isTrue();
        
        // Проверка отображения таблицы оценок
        WebElement scoresTable = driver.findElement(By.id("scores-table"));
        assertThat(scoresTable.isDisplayed()).isTrue();
        
        // Проверка заголовков таблицы
        List<WebElement> headers = scoresTable.findElements(By.tagName("th"));
        assertThat(headers.size()).isGreaterThan(0);
        assertThat(headers.get(0).getText()).contains("Судья");
        
        // Проверка наличия строк с оценками
        List<WebElement> scoreRows = scoresTable.findElements(By.cssSelector("tbody tr"));
        assertThat(scoreRows.size()).isGreaterThan(0);
        
        log.info("✓ Судейские оценки отображаются корректно");
    }

    @Test
    @DisplayName("Отображение графиков и визуализации")
    void testChartsAndVisualization() {
        log.info("Тест: Отображение графиков и визуализации");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия секции графиков
        WebElement chartsSection = driver.findElement(By.id("charts-section"));
        assertThat(chartsSection.isDisplayed()).isTrue();
        
        // Проверка наличия canvas элементов для графиков
        List<WebElement> chartCanvases = driver.findElements(By.tagName("canvas"));
        assertThat(chartCanvases.size()).isGreaterThan(0);
        
        // Проверка наличия кнопок переключения графиков
        List<WebElement> chartButtons = driver.findElements(By.className("chart-toggle"));
        assertThat(chartButtons.size()).isGreaterThan(0);
        
        log.info("✓ Графики и визуализация отображаются корректно");
    }

    @Test
    @DisplayName("Переключение между различными графиками")
    void testChartSwitching() {
        log.info("Тест: Переключение между различными графиками");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия кнопок переключения
        List<WebElement> chartButtons = driver.findElements(By.className("chart-toggle"));
        assertThat(chartButtons.size()).isGreaterThan(1);
        
        // Клик по второй кнопке
        WebElement secondChartButton = chartButtons.get(1);
        String buttonText = secondChartButton.getText();
        secondChartButton.click();
        
        // Проверка, что активная кнопка изменилась
        WebElement activeButton = driver.findElement(By.cssSelector(".chart-toggle.active"));
        assertThat(activeButton.getText()).isEqualTo(buttonText);
        
        log.info("✓ Переключение между графиками работает корректно");
    }

    @Test
    @DisplayName("Отображение кнопок действий")
    void testActionButtonsDisplay() {
        log.info("Тест: Отображение кнопок действий");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия кнопки редактирования
        WebElement editButton = driver.findElement(By.linkText("Редактировать"));
        assertThat(editButton.isDisplayed()).isTrue();
        assertThat(editButton.getAttribute("href")).contains("/fights/" + fightId + "/edit");
        
        // Проверка наличия кнопки удаления
        WebElement deleteButton = driver.findElement(By.cssSelector("button[data-action='delete']"));
        assertThat(deleteButton.isDisplayed()).isTrue();
        
        // Проверка наличия кнопки "Назад к списку"
        WebElement backButton = driver.findElement(By.linkText("Назад к списку"));
        assertThat(backButton.isDisplayed()).isTrue();
        assertThat(backButton.getAttribute("href")).contains("/fights");
        
        log.info("✓ Кнопки действий отображаются корректно");
    }

    @Test
    @DisplayName("Переход к редактированию боя")
    void testNavigateToEditFight() {
        log.info("Тест: Переход к редактированию боя");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Клик по кнопке редактирования
        WebElement editButton = driver.findElement(By.linkText("Редактировать"));
        editButton.click();
        
        // Проверка перенаправления на страницу редактирования
        wait.until(ExpectedConditions.urlContains("/fights/" + fightId + "/edit"));
        
        // Проверка, что мы на странице редактирования
        WebElement title = driver.findElement(By.tagName("h1"));
        assertThat(title.getText()).contains("Редактирование боя");
        
        log.info("✓ Переход к редактированию боя работает корректно");
    }

    @Test
    @DisplayName("Возврат к списку боев")
    void testNavigateBackToFightsList() {
        log.info("Тест: Возврат к списку боев");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Клик по кнопке "Назад к списку"
        WebElement backButton = driver.findElement(By.linkText("Назад к списку"));
        backButton.click();
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Проверка, что мы на странице списка боев
        WebElement fightsTable = driver.findElement(By.id("fights-table"));
        assertThat(fightsTable.isDisplayed()).isTrue();
        
        log.info("✓ Возврат к списку боев работает корректно");
    }

    @Test
    @DisplayName("Удаление боя со страницы деталей")
    void testDeleteFightFromDetailsPage() {
        log.info("Тест: Удаление боя со страницы деталей");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Клик по кнопке удаления
        WebElement deleteButton = driver.findElement(By.cssSelector("button[data-action='delete']"));
        deleteButton.click();
        
        // Подтверждение удаления в диалоге
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Проверка, что бой удален (не должен отображаться в списке)
        WebElement fightsTable = driver.findElement(By.id("fights-table"));
        assertThat(fightsTable.getText()).doesNotContain("Тестовый боец");
        
        log.info("✓ Удаление боя со страницы деталей работает корректно");
    }

    @Test
    @DisplayName("Отображение заметок о бое")
    void testFightNotesDisplay() {
        log.info("Тест: Отображение заметок о бое");
        
        // Создаем тестовый бой с заметками
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия секции заметок
        WebElement notesSection = driver.findElement(By.id("fight-notes"));
        assertThat(notesSection.isDisplayed()).isTrue();
        
        // Проверка отображения заметок
        WebElement notesContent = driver.findElement(By.className("notes-content"));
        assertThat(notesContent.getText()).contains("Отличный бой с хорошей техникой");
        
        log.info("✓ Заметки о бое отображаются корректно");
    }

    @Test
    @DisplayName("Отображение метаданных боя")
    void testFightMetadataDisplay() {
        log.info("Тест: Отображение метаданных боя");
        
        // Создаем тестовый бой
        Long fightId = createTestFightWithDetails();
        
        // Переход на страницу деталей боя
        navigateTo("/fights/" + fightId);
        waitForPageLoad();
        
        // Проверка наличия секции метаданных
        WebElement metadataSection = driver.findElement(By.id("fight-metadata"));
        assertThat(metadataSection.isDisplayed()).isTrue();
        
        // Проверка отображения ключевых метаданных
        assertThat(metadataSection.getText()).contains("Сезон: 1");
        assertThat(metadataSection.getText()).contains("Рейтинговые очки: 150");
        assertThat(metadataSection.getText()).contains("Позиция в рейтинге: 3");
        
        log.info("✓ Метаданные боя отображаются корректно");
    }

    /**
     * Создание тестового боя с детальной информацией
     */
    private Long createTestFightWithDetails() {
        log.info("Создание тестового боя с детальной информацией");
        
        // Переход на страницу создания боя
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение формы
        fillDetailedFightForm();
        
        // Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        // Ожидание перенаправления
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Получение ID созданного боя из URL или другим способом
        // В реальном приложении это может быть реализовано по-разному
        return 1L; // Заглушка для демонстрации
    }

    /**
     * Заполнение детальной формы боя
     */
    private void fillDetailedFightForm() {
        // Основные поля
        driver.findElement(By.id("myFighter")).sendKeys("Тестовый боец");
        driver.findElement(By.id("opponent")).sendKeys("Тестовый соперник");
        
        // Выбор режима, результата и метода
        driver.findElement(By.id("mode")).sendKeys("ММА");
        driver.findElement(By.id("result")).sendKeys("Победа");
        driver.findElement(By.id("method")).sendKeys("Нокаут");
        
        // Дополнительные поля
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("150");
        driver.findElement(By.id("rankingPosition")).sendKeys("3");
        driver.findElement(By.id("notes")).sendKeys("Отличный бой с хорошей техникой");
        
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
        driver.findElement(By.id("round-1-my-significant-strikes-landed")).sendKeys("15");
        driver.findElement(By.id("round-1-my-significant-strikes-attempted")).sendKeys("25");
        driver.findElement(By.id("round-1-my-takedowns-successful")).sendKeys("2");
        driver.findElement(By.id("round-1-my-takedowns-attempted")).sendKeys("3");
        driver.findElement(By.id("round-1-my-control-time")).sendKeys("01:30");
        
        driver.findElement(By.id("round-1-opponent-significant-strikes-landed")).sendKeys("12");
        driver.findElement(By.id("round-1-opponent-significant-strikes-attempted")).sendKeys("20");
        driver.findElement(By.id("round-1-opponent-takedowns-successful")).sendKeys("1");
        driver.findElement(By.id("round-1-opponent-takedowns-attempted")).sendKeys("2");
        driver.findElement(By.id("round-1-opponent-control-time")).sendKeys("00:45");
    }

    /**
     * Заполнение судейских оценок
     */
    private void fillJudgeScores() {
        // Оценки первого судьи
        driver.findElement(By.id("judge-1-round-1-my-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-1-opponent-score")).sendKeys("9");
        driver.findElement(By.id("judge-1-round-2-my-score")).sendKeys("9");
        driver.findElement(By.id("judge-1-round-2-opponent-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-3-my-score")).sendKeys("10");
        driver.findElement(By.id("judge-1-round-3-opponent-score")).sendKeys("9");
    }
}
