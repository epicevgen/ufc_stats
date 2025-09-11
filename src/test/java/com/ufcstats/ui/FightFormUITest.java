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
 * UI тесты для формы создания/редактирования боя
 */
@Slf4j
@DisplayName("UI тесты формы боя")
class FightFormUITest extends BaseUITest {

    @Test
    @DisplayName("Отображение формы создания боя")
    void testNewFightFormDisplay() {
        log.info("Тест: Отображение формы создания боя");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Проверка заголовка
        WebElement title = driver.findElement(By.tagName("h1"));
        assertThat(title.getText()).contains("Новый бой");
        
        // Проверка наличия всех обязательных полей
        assertThat(driver.findElement(By.id("myFighter")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("opponent")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("mode")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("result")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("method")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("season")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("ratingPoints")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("rankingPosition")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("fightDate")).isDisplayed()).isTrue();
        
        // Проверка кнопки отправки
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertThat(submitButton.isDisplayed()).isTrue();
        assertThat(submitButton.getText()).contains("Сохранить");
        
        log.info("✓ Форма создания боя отображается корректно");
    }

    @Test
    @DisplayName("Заполнение всех полей формы")
    void testFillAllFormFields() {
        log.info("Тест: Заполнение всех полей формы");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение всех полей формы включая динамические
        fillCompleteFightForm("Иван Петров", "Алексей Сидоров", "ММА", "Победа", "Нокаут");
        
        // Проверка, что все основные поля заполнены
        assertThat(driver.findElement(By.id("myFighter")).getAttribute("value")).isEqualTo("Иван Петров");
        assertThat(driver.findElement(By.id("opponent")).getAttribute("value")).isEqualTo("Алексей Сидоров");
        
        Select modeSelect = new Select(driver.findElement(By.id("mode")));
        assertThat(modeSelect.getFirstSelectedOption().getText()).isEqualTo("ММА");
        
        Select resultSelect = new Select(driver.findElement(By.id("result")));
        assertThat(resultSelect.getFirstSelectedOption().getText()).isEqualTo("Победа");
        
        Select methodSelect = new Select(driver.findElement(By.id("method")));
        assertThat(methodSelect.getFirstSelectedOption().getText()).isEqualTo("Нокаут");
        
        assertThat(driver.findElement(By.id("season")).getAttribute("value")).isEqualTo("1");
        assertThat(driver.findElement(By.id("ratingPoints")).getAttribute("value")).isEqualTo("100");
        assertThat(driver.findElement(By.id("rankingPosition")).getAttribute("value")).isEqualTo("5");
        assertThat(driver.findElement(By.id("notes")).getAttribute("value")).isEqualTo("Тестовые примечания к бою для UI тестирования");
        
        // Проверка, что динамические поля для раундов созданы и заполнены
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("round1_my_head_damage")));
        assertThat(driver.findElement(By.id("round1_my_head_damage")).getAttribute("value")).isEqualTo("25");
        assertThat(driver.findElement(By.id("round1_opponent_head_damage")).getAttribute("value")).isEqualTo("20");
        
        // Проверка, что судейские оценки заполнены
        assertThat(driver.findElement(By.id("judge1_my_round1")).getAttribute("value")).isEqualTo("10");
        assertThat(driver.findElement(By.id("judge1_opponent_round1")).getAttribute("value")).isEqualTo("9");
        
        log.info("✓ Все поля формы заполнены корректно");
    }

    @Test
    @DisplayName("Валидация обязательных полей")
    void testRequiredFieldsValidation() {
        log.info("Тест: Валидация обязательных полей");
        
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
        
        // Проверка конкретных ошибок
        boolean hasMyFighterError = errorMessages.stream()
                .anyMatch(error -> error.getText().contains("Имя бойца обязательно"));
        boolean hasOpponentError = errorMessages.stream()
                .anyMatch(error -> error.getText().contains("Имя соперника обязательно"));
        
        assertThat(hasMyFighterError || hasOpponentError).isTrue();
        
        log.info("✓ Валидация обязательных полей работает корректно");
    }

    @Test
    @DisplayName("Валидация числовых полей")
    void testNumericFieldsValidation() {
        log.info("Тест: Валидация числовых полей");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение полей некорректными значениями
        driver.findElement(By.id("myFighter")).sendKeys("Тест");
        driver.findElement(By.id("opponent")).sendKeys("Тест");
        
        // Некорректные числовые значения
        driver.findElement(By.id("season")).sendKeys("abc");
        driver.findElement(By.id("ratingPoints")).sendKeys("-100");
        driver.findElement(By.id("rankingPosition")).sendKeys("0");
        
        // Выбор обязательных полей
        WebElement modeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mode")));
        Select modeSelect = new Select(modeElement);
        modeSelect.selectByValue("MMA");
        
        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
        Select resultSelect = new Select(resultElement);
        resultSelect.selectByValue("WIN");
        
        WebElement methodElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("method")));
        Select methodSelect = new Select(methodElement);
        methodSelect.selectByValue("KNOCKOUT");
        
        // Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        // Используем JavaScript для клика, чтобы избежать ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверка ошибок валидации
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("invalid-feedback")));
        
        List<WebElement> errorMessages = driver.findElements(By.className("invalid-feedback"));
        assertThat(errorMessages.size()).isGreaterThan(0);
        
        log.info("✓ Валидация числовых полей работает корректно");
    }

    @Test
    @DisplayName("Динамическое добавление раундов")
    void testDynamicRoundsAddition() {
        log.info("Тест: Динамическое добавление раундов");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение основных полей
        fillBasicFightFields();
        
        // Проверка начального количества раундов (должно быть 3)
        List<WebElement> roundSections = driver.findElements(By.className("round-section"));
        assertThat(roundSections.size()).isEqualTo(3);
        
        // Добавление дополнительного раунда
        WebElement addRoundButton = driver.findElement(By.id("add-round"));
        addRoundButton.click();
        
        // Проверка, что раунд добавился
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("round-section"), 4));
        
        // Удаление раунда
        WebElement removeRoundButton = driver.findElement(By.cssSelector(".remove-round"));
        removeRoundButton.click();
        
        // Проверка, что раунд удалился
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("round-section"), 3));
        
        log.info("✓ Динамическое управление раундами работает корректно");
    }

    @Test
    @DisplayName("Динамическое добавление судейских оценок")
    void testDynamicJudgeScoresAddition() {
        log.info("Тест: Динамическое добавление судейских оценок");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение основных полей
        fillBasicFightFields();
        
        // Проверка начального количества судей (должно быть 3)
        List<WebElement> judgeSections = driver.findElements(By.className("judge-section"));
        assertThat(judgeSections.size()).isEqualTo(3);
        
        // Добавление дополнительного судьи
        WebElement addJudgeButton = driver.findElement(By.id("add-judge"));
        addJudgeButton.click();
        
        // Проверка, что судья добавился
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("judge-section"), 4));
        
        // Удаление судьи
        WebElement removeJudgeButton = driver.findElement(By.cssSelector(".remove-judge"));
        removeJudgeButton.click();
        
        // Проверка, что судья удалился
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("judge-section"), 3));
        
        log.info("✓ Динамическое управление судейскими оценками работает корректно");
    }

    @Test
    @DisplayName("Заполнение статистики раундов")
    void testRoundStatisticsFilling() {
        log.info("Тест: Заполнение статистики раундов");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение основных полей
        fillBasicFightFields();
        
        // Заполнение статистики первого раунда
        WebElement firstRound = driver.findElement(By.id("round-1"));
        
        // Моя статистика
        firstRound.findElement(By.id("round-1-my-significant-strikes-landed")).sendKeys("15");
        firstRound.findElement(By.id("round-1-my-significant-strikes-attempted")).sendKeys("25");
        firstRound.findElement(By.id("round-1-my-takedowns-successful")).sendKeys("2");
        firstRound.findElement(By.id("round-1-my-takedowns-attempted")).sendKeys("3");
        firstRound.findElement(By.id("round-1-my-control-time")).sendKeys("01:30");
        
        // Статистика соперника
        firstRound.findElement(By.id("round-1-opponent-significant-strikes-landed")).sendKeys("12");
        firstRound.findElement(By.id("round-1-opponent-significant-strikes-attempted")).sendKeys("20");
        firstRound.findElement(By.id("round-1-opponent-takedowns-successful")).sendKeys("1");
        firstRound.findElement(By.id("round-1-opponent-takedowns-attempted")).sendKeys("2");
        firstRound.findElement(By.id("round-1-opponent-control-time")).sendKeys("00:45");
        
        // Проверка, что данные сохранились
        assertThat(firstRound.findElement(By.id("round-1-my-significant-strikes-landed")).getAttribute("value")).isEqualTo("15");
        assertThat(firstRound.findElement(By.id("round-1-opponent-significant-strikes-landed")).getAttribute("value")).isEqualTo("12");
        
        log.info("✓ Статистика раундов заполняется корректно");
    }

    @Test
    @DisplayName("Заполнение судейских оценок")
    void testJudgeScoresFilling() {
        log.info("Тест: Заполнение судейских оценок");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение основных полей
        fillBasicFightFields();
        
        // Заполнение оценок первого судьи
        WebElement firstJudge = driver.findElement(By.id("judge-1"));
        
        // Оценки по раундам
        firstJudge.findElement(By.id("judge-1-round-1-my-score")).sendKeys("10");
        firstJudge.findElement(By.id("judge-1-round-1-opponent-score")).sendKeys("9");
        firstJudge.findElement(By.id("judge-1-round-2-my-score")).sendKeys("9");
        firstJudge.findElement(By.id("judge-1-round-2-opponent-score")).sendKeys("10");
        firstJudge.findElement(By.id("judge-1-round-3-my-score")).sendKeys("10");
        firstJudge.findElement(By.id("judge-1-round-3-opponent-score")).sendKeys("9");
        
        // Проверка, что данные сохранились
        assertThat(firstJudge.findElement(By.id("judge-1-round-1-my-score")).getAttribute("value")).isEqualTo("10");
        assertThat(firstJudge.findElement(By.id("judge-1-round-1-opponent-score")).getAttribute("value")).isEqualTo("9");
        
        log.info("✓ Судейские оценки заполняются корректно");
    }

    @Test
    @DisplayName("Отмена создания боя")
    void testCancelFightCreation() {
        log.info("Тест: Отмена создания боя");
        
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Заполнение формы
        fillBasicFightFields();
        
        // Клик по кнопке отмены
        WebElement cancelButton = driver.findElement(By.linkText("Отмена"));
        cancelButton.click();
        
        // Проверка перенаправления на список боев
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        log.info("✓ Отмена создания боя работает корректно");
    }

    /**
     * Заполнение основных полей формы боя
     */
    private void fillBasicFightFields() {
        driver.findElement(By.id("myFighter")).sendKeys("Тестовый боец");
        driver.findElement(By.id("opponent")).sendKeys("Тестовый соперник");
        
        WebElement modeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mode")));
        Select modeSelect = new Select(modeElement);
        modeSelect.selectByValue("MMA");
        
        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
        Select resultSelect = new Select(resultElement);
        resultSelect.selectByValue("WIN");
        
        WebElement methodElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("method")));
        Select methodSelect = new Select(methodElement);
        methodSelect.selectByValue("KNOCKOUT");
        
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("100");
        driver.findElement(By.id("rankingPosition")).sendKeys("5");
        
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        driver.findElement(By.id("fightDate")).clear();
        driver.findElement(By.id("fightDate")).sendKeys(currentDate);
    }
    
    /**
     * Заполнение всех полей формы боя включая динамические
     */
    private void fillCompleteFightForm(String myFighter, String opponent, String mode, String result, String method) {
        log.info("Заполнение полной формы боя: {} vs {}, режим: {}, результат: {}, метод: {}", 
                myFighter, opponent, mode, result, method);
        
        // Заполнение основных полей
        driver.findElement(By.id("myFighter")).sendKeys(myFighter);
        driver.findElement(By.id("opponent")).sendKeys(opponent);
        
        // Выбор режима боя
        WebElement modeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mode")));
        Select modeSelect = new Select(modeElement);
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
}
