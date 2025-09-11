package com.ufcstats.ui;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI тесты для CRUD операций с боями
 */
@Slf4j
@DisplayName("UI тесты CRUD операций с боями")
class FightCRUDUITest extends BaseUITest {

    @Test
    @DisplayName("Просмотр списка боев")
    void testViewFightsList() {
        log.info("Тест: Просмотр списка боев");
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("UFC Stats");
        
        // Проверка наличия таблицы боев
        WebElement fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.isDisplayed()).isTrue();
        
        // Проверка наличия кнопки "Новый бой"
        WebElement newFightButton = driver.findElement(By.linkText("Новый бой"));
        assertThat(newFightButton.isDisplayed()).isTrue();
        
        log.info("✓ Список боев отображается корректно");
    }

    @Test
    @DisplayName("Создание нового боя")
    void testCreateNewFight() {
        log.info("Тест: Создание нового боя");
        
        // Переход на страницу создания боя
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("Новый бой");
        
        // Заполнение формы
        fillFightForm("Тестовый боец", "Тестовый соперник", "ММА", "Победа", "Нокаут");
        
        // Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        // Прокручиваем к кнопке и ждем, пока она станет кликабельной
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Проверка, что новый бой появился в списке
        WebElement fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.getText()).contains("Тестовый боец");
        assertThat(fightsTable.getText()).contains("Тестовый соперник");
        
        log.info("✓ Новый бой успешно создан");
    }

    @Test
    @DisplayName("Просмотр деталей боя")
    void testViewFightDetails() {
        log.info("Тест: Просмотр деталей боя");
        
        // Сначала создаем бой для просмотра
        createTestFight();
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по ссылке "Просмотр" первого боя
        WebElement viewLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[href*='/fights/'][href*='/view']")
        ));
        viewLink.click();
        
        // Проверка, что мы на странице деталей боя
        wait.until(ExpectedConditions.urlContains("/fights/"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fight-details")));
        
        // Проверка отображения информации о бое
        WebElement fightDetails = driver.findElement(By.id("fight-details"));
        assertThat(fightDetails.isDisplayed()).isTrue();
        
        // Проверка наличия кнопки редактирования
        WebElement editButton = driver.findElement(By.linkText("Редактировать"));
        assertThat(editButton.isDisplayed()).isTrue();
        
        log.info("✓ Детали боя отображаются корректно");
    }

    @Test
    @DisplayName("Редактирование боя")
    void testEditFight() {
        log.info("Тест: Редактирование боя");
        
        // Сначала создаем бой для редактирования
        createTestFight();
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по ссылке "Редактировать" первого боя
        WebElement editLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[href*='/fights/'][href*='/edit']")
        ));
        editLink.click();
        
        // Проверка, что мы на странице редактирования
        wait.until(ExpectedConditions.urlContains("/edit"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fight-form")));
        
        // Изменение данных в форме - заполняем все поля
        fillFightForm("Обновленный боец", "Обновленный соперник", "ММА", "Победа", "Сабмишен");
        
        // Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        // Прокручиваем к кнопке и ждем, пока она станет кликабельной
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Проверка, что изменения сохранились
        WebElement fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.getText()).contains("Обновленный боец");
        
        log.info("✓ Бой успешно отредактирован");
    }

    @Test
    @DisplayName("Удаление боя")
    void testDeleteFight() {
        log.info("Тест: Удаление боя");
        
        // Сначала создаем бой для удаления
        createTestFight();
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Запоминаем количество боев до удаления
        List<WebElement> fightRows = driver.findElements(By.cssSelector("#fights-table tbody tr"));
        int initialCount = fightRows.size();
        
        // Клик по кнопке удаления первого боя
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-action='delete']")
        ));
        deleteButton.click();
        
        // Подтверждение удаления в диалоге
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        // Проверка, что количество боев уменьшилось
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#fights-table tbody tr"), initialCount - 1));
        
        log.info("✓ Бой успешно удален");
    }

    @Test
    @DisplayName("Валидация формы создания боя")
    void testFightFormValidation() {
        log.info("Тест: Валидация формы создания боя");
        
        // Переход на страницу создания боя
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Попытка отправить пустую форму
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверка отображения ошибок валидации
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("invalid-feedback")));
        
        List<WebElement> errorMessages = driver.findElements(By.className("invalid-feedback"));
        assertThat(errorMessages.size()).isGreaterThan(0);
        
        // Теперь заполняем все обязательные поля и проверяем, что валидация проходит
        fillFightForm("Валидационный боец", "Валидационный соперник", "ММА", "Победа", "Нокаут");
        
        // Отправляем форму с заполненными полями
        // Прокручиваем к кнопке и ждем, пока она станет кликабельной
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверяем, что мы перенаправлены на список боев (валидация прошла)
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        log.info("✓ Валидация формы работает корректно");
    }

    /**
     * Заполнение формы боя
     */
    private void fillFightForm(String myFighter, String opponent, String mode, String result, String method) {
        log.info("Заполнение формы боя: {} vs {}, режим: {}, результат: {}, метод: {}", 
                myFighter, opponent, mode, result, method);
        
        // Заполнение основных полей
        driver.findElement(By.id("myFighter")).sendKeys(myFighter);
        driver.findElement(By.id("opponent")).sendKeys(opponent);
        
        // Выбор режима боя
        WebElement modeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mode")));
        Select modeSelect = new Select(modeElement);
        // Используем value вместо visible text
        if ("ММА".equals(mode)) {
            modeSelect.selectByValue("MMA");
        } else if ("Стойка".equals(mode)) {
            modeSelect.selectByValue("STANCE");
        } else {
            modeSelect.selectByVisibleText(mode);
        }
        
        // Выбор результата
        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
        Select resultSelect = new Select(resultElement);
        // Используем value вместо visible text
        if ("Победа".equals(result)) {
            resultSelect.selectByValue("WIN");
        } else if ("Поражение".equals(result)) {
            resultSelect.selectByValue("LOSS");
        } else if ("Ничья".equals(result)) {
            resultSelect.selectByValue("DRAW");
        } else {
            resultSelect.selectByVisibleText(result);
        }
        
        // Выбор метода
        WebElement methodElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("method")));
        Select methodSelect = new Select(methodElement);
        // Используем value вместо visible text
        if ("Нокаут".equals(method)) {
            methodSelect.selectByValue("KNOCKOUT");
        } else if ("Сабмишен".equals(method)) {
            methodSelect.selectByValue("SUBMISSION");
        } else if ("Решение".equals(method)) {
            methodSelect.selectByValue("DECISION");
        } else if ("Досрочный выход".equals(method)) {
            methodSelect.selectByValue("RETIREMENT");
        } else {
            methodSelect.selectByVisibleText(method);
        }
        
        // Заполнение дополнительных полей
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("100");
        driver.findElement(By.id("rankingPosition")).sendKeys("5");
        
        // Установка даты боя
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        driver.findElement(By.id("fightDate")).clear();
        driver.findElement(By.id("fightDate")).sendKeys(currentDate);
        
        // Выбор весовой категории
        WebElement weightClassElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("weightClass")));
        Select weightClassSelect = new Select(weightClassElement);
        weightClassSelect.selectByValue("MIDDLEWEIGHT");
        
        // Установка количества раундов (это вызовет обновление динамических полей)
        WebElement roundsPlayedInput = driver.findElement(By.id("roundsPlayed"));
        roundsPlayedInput.clear();
        roundsPlayedInput.sendKeys("3");
        
        // Ждем, пока динамические поля загрузятся
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("round1_my_head_damage")));
        
        // Заполнение статистики по раундам
        fillRoundStatistics();
        
        // Заполнение судейских оценок
        fillJudgeScores();
        
        // Заполнение примечаний
        WebElement notesField = driver.findElement(By.id("notes"));
        notesField.sendKeys("Тестовые примечания к бою для UI тестирования");
    }
    
    /**
     * Заполнение статистики по раундам
     */
    private void fillRoundStatistics() {
        log.info("Заполнение статистики по раундам");
        
        // Заполняем статистику для 3 раундов
        for (int round = 1; round <= 3; round++) {
            // Статистика моего бойца
            fillRoundField(round, "my", "head_damage", "25");
            fillRoundField(round, "my", "body_damage", "15");
            fillRoundField(round, "my", "leg_damage", "10");
            fillRoundField(round, "my", "knockdowns", "1");
            fillRoundField(round, "my", "significant_landed", "20");
            fillRoundField(round, "my", "significant_attempted", "35");
            fillRoundField(round, "my", "total_landed", "45");
            fillRoundField(round, "my", "total_attempted", "60");
            fillRoundField(round, "my", "takedowns_successful", "2");
            fillRoundField(round, "my", "takedowns_attempted", "3");
            fillRoundField(round, "my", "control_time", "02:30");
            
            // Статистика соперника
            fillRoundField(round, "opponent", "head_damage", "20");
            fillRoundField(round, "opponent", "body_damage", "12");
            fillRoundField(round, "opponent", "leg_damage", "8");
            fillRoundField(round, "opponent", "knockdowns", "0");
            fillRoundField(round, "opponent", "significant_landed", "18");
            fillRoundField(round, "opponent", "significant_attempted", "32");
            fillRoundField(round, "opponent", "total_landed", "40");
            fillRoundField(round, "opponent", "total_attempted", "55");
            fillRoundField(round, "opponent", "takedowns_successful", "1");
            fillRoundField(round, "opponent", "takedowns_attempted", "2");
            fillRoundField(round, "opponent", "control_time", "01:45");
        }
    }
    
    /**
     * Заполнение поля раунда
     */
    private void fillRoundField(int round, String fighter, String field, String value) {
        String fieldId = String.format("round%d_%s_%s", round, fighter, field);
        try {
            WebElement fieldElement = driver.findElement(By.id(fieldId));
            fieldElement.clear();
            fieldElement.sendKeys(value);
        } catch (Exception e) {
            log.warn("Не удалось заполнить поле {}: {}", fieldId, e.getMessage());
        }
    }
    
    /**
     * Заполнение судейских оценок
     */
    private void fillJudgeScores() {
        log.info("Заполнение судейских оценок");
        
        // Заполняем оценки для 3 судей
        for (int judge = 1; judge <= 3; judge++) {
            // Оценки моего бойца
            fillJudgeScore(judge, "my", 1, "10");
            fillJudgeScore(judge, "my", 2, "9");
            fillJudgeScore(judge, "my", 3, "10");
            fillJudgeScore(judge, "my", 4, "0"); // 4-й раунд не игрался
            fillJudgeScore(judge, "my", 5, "0"); // 5-й раунд не игрался
            
            // Оценки соперника
            fillJudgeScore(judge, "opponent", 1, "9");
            fillJudgeScore(judge, "opponent", 2, "10");
            fillJudgeScore(judge, "opponent", 3, "9");
            fillJudgeScore(judge, "opponent", 4, "0"); // 4-й раунд не игрался
            fillJudgeScore(judge, "opponent", 5, "0"); // 5-й раунд не игрался
        }
    }
    
    /**
     * Заполнение оценки судьи
     */
    private void fillJudgeScore(int judge, String fighter, int round, String score) {
        String fieldId = String.format("judge%d_%s_round%d", judge, fighter, round);
        try {
            WebElement fieldElement = driver.findElement(By.id(fieldId));
            fieldElement.clear();
            fieldElement.sendKeys(score);
        } catch (Exception e) {
            log.warn("Не удалось заполнить поле {}: {}", fieldId, e.getMessage());
        }
    }

    /**
     * Создание тестового боя
     */
    private void createTestFight() {
        log.info("Создание тестового боя");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        fillFightForm("Тестовый боец", "Тестовый соперник", "ММА", "Победа", "Нокаут");
        
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        wait.until(ExpectedConditions.urlContains("/fights"));
    }
}
