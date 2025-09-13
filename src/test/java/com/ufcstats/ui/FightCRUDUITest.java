package com.ufcstats.ui;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

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
        assertThat(getPageTitle()).contains("Список боев");
        
        // Проверка наличия таблицы боев
        WebElement fightsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        assertThat(fightsTable.isDisplayed()).isTrue();
        
        // Проверка наличия кнопки "Добавить бой" для модального окна
        WebElement addFightButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button[data-bs-target='#newFightModal']")
        ));
        assertThat(addFightButton.isDisplayed()).isTrue();
        
        // Проверка наличия модальных окон
        WebElement newFightModal = driver.findElement(By.id("newFightModal"));
        assertThat(newFightModal.isDisplayed()).isFalse(); // Модальное окно должно быть скрыто
        
        WebElement editFightModal = driver.findElement(By.id("editFightModal"));
        assertThat(editFightModal.isDisplayed()).isFalse(); // Модальное окно должно быть скрыто
        
        WebElement viewFightModal = driver.findElement(By.id("viewFightModal"));
        assertThat(viewFightModal.isDisplayed()).isFalse(); // Модальное окно должно быть скрыто
        
        log.info("✓ Список боев отображается корректно с модальными окнами");
    }

    @Test
    @DisplayName("Создание нового боя через модальное окно")
    void testCreateNewFight() {
        log.info("Тест: Создание нового боя через модальное окно");
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("Список боев");
        
        // Клик по кнопке "Добавить бой" для открытия модального окна
        WebElement addFightButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-bs-target='#newFightModal']")
        ));
        addFightButton.click();
        
        // Ждем, пока модальное окно откроется и форма загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightFormContainer")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#newFightFormContainer form")));
        
        // Заполнение формы в модальном окне
        fillFightFormInModal("Тестовый боец", "Тестовый соперник", "ММА", "Победа", "Нокаут");
        
        // Отправка формы
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#newFightFormContainer button[type='submit']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Ждем, пока модальное окно закроется
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("newFightModal")));
        
        // Ждем, пока страница обновится и таблица загрузится
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        
        // Проверка, что новый бой появился в списке
        WebElement fightsTable = driver.findElement(By.id("fights-table"));
        assertThat(fightsTable.getText()).contains("Тестовый боец");
        assertThat(fightsTable.getText()).contains("Тестовый соперник");
        
        log.info("✓ Новый бой успешно создан через модальное окно");
    }

    @Test
    @DisplayName("Просмотр деталей боя через модальное окно")
    void testViewFightDetails() {
        log.info("Тест: Просмотр деталей боя через модальное окно");
        
        // Сначала создаем бой для просмотра
        createTestFightInModal();
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по кнопке "Просмотр" первого боя (теперь это кнопка, а не ссылка)
        WebElement viewButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[onclick*='viewFight']")
        ));
        viewButton.click();
        
        // Ждем, пока модальное окно откроется и содержимое загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewFightModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewFightFormContainer")));
        
        // Проверка отображения информации о бое в модальном окне
        WebElement fightDetails = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("#viewFightFormContainer .fight-details, #viewFightFormContainer .card")
        ));
        assertThat(fightDetails.isDisplayed()).isTrue();
        
        // Проверка наличия кнопки закрытия модального окна
        WebElement closeButton = driver.findElement(By.cssSelector("#viewFightModal .btn-close"));
        assertThat(closeButton.isDisplayed()).isTrue();
        
        // Закрываем модальное окно
        closeButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("viewFightModal")));
        
        log.info("✓ Детали боя отображаются корректно в модальном окне");
    }

    @Test
    @DisplayName("Редактирование боя через модальное окно")
    void testEditFight() {
        log.info("Тест: Редактирование боя через модальное окно");
        
        // Сначала создаем бой для редактирования
        createTestFightInModal();
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по кнопке "Редактировать" первого боя (теперь это кнопка, а не ссылка)
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[onclick*='editFight']")
        ));
        editButton.click();
        
        // Ждем, пока модальное окно откроется и форма загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editFightModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editFightFormContainer")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#editFightFormContainer form")));
        
        // Изменение данных в форме - заполняем все поля
        fillFightFormInModal("Обновленный боец", "Обновленный соперник", "ММА", "Победа", "Сабмишен");
        
        // Отправка формы
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#editFightFormContainer button[type='submit']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Ждем, пока модальное окно закроется
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editFightModal")));
        
        // Ждем, пока страница обновится и таблица загрузится
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        
        // Проверка, что изменения сохранились
        WebElement fightsTable = driver.findElement(By.id("fights-table"));
        assertThat(fightsTable.getText()).contains("Обновленный боец");
        
        log.info("✓ Бой успешно отредактирован через модальное окно");
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
    @DisplayName("Валидация формы создания боя в модальном окне")
    void testFightFormValidation() {
        log.info("Тест: Валидация формы создания боя в модальном окне");
        
        // Переход на страницу списка боев
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по кнопке "Добавить бой" для открытия модального окна
        WebElement addFightButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-bs-target='#newFightModal']")
        ));
        addFightButton.click();
        
        // Ждем, пока модальное окно откроется и форма загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightFormContainer")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#newFightFormContainer form")));
        
        // Попытка отправить пустую форму
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#newFightFormContainer button[type='submit']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Проверка отображения ошибок валидации
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("invalid-feedback")));
        
        List<WebElement> errorMessages = driver.findElements(By.className("invalid-feedback"));
        assertThat(errorMessages.size()).isGreaterThan(0);
        
        // Теперь заполняем все обязательные поля и проверяем, что валидация проходит
        fillFightFormInModal("Валидационный боец", "Валидационный соперник", "ММА", "Победа", "Нокаут");
        
        // Отправляем форму с заполненными полями
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Ждем, пока модальное окно закроется (валидация прошла)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("newFightModal")));
        
        // Ждем, пока страница обновится и таблица загрузится
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fights-table")));
        
        // Проверяем, что новый бой появился в списке
        WebElement fightsTable = driver.findElement(By.id("fights-table"));
        assertThat(fightsTable.getText()).contains("Валидационный боец");
        
        log.info("✓ Валидация формы работает корректно в модальном окне");
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
        
        // Установка даты боя (используем фиксированную дату в прошлом)
        String fightDate = "2024-01-15T20:00";
        WebElement fightDateElement = driver.findElement(By.id("fightDate"));
        fightDateElement.clear();
        fightDateElement.sendKeys(fightDate);
        
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
        
        // Заполняем статистику для 3 раундов (только существующие поля)
        for (int round = 1; round <= 3; round++) {
            // Статистика моего бойца
            fillRoundField(round, "my", "head_damage", "25");
            fillRoundField(round, "my", "body_damage", "15");
            fillRoundField(round, "my", "leg_damage", "10");
            fillRoundField(round, "my", "knockdowns", "1");
            
            // Статистика соперника
            fillRoundField(round, "opponent", "head_damage", "20");
            fillRoundField(round, "opponent", "body_damage", "12");
            fillRoundField(round, "opponent", "leg_damage", "8");
            fillRoundField(round, "opponent", "knockdowns", "0");
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
        
        // Заполняем оценки для 3 судей (только для 3 раундов)
        for (int judge = 1; judge <= 3; judge++) {
            // Оценки моего бойца
            fillJudgeScore(judge, "my", 1, "10");
            fillJudgeScore(judge, "my", 2, "9");
            fillJudgeScore(judge, "my", 3, "10");
            
            // Оценки соперника
            fillJudgeScore(judge, "opponent", 1, "9");
            fillJudgeScore(judge, "opponent", 2, "10");
            fillJudgeScore(judge, "opponent", 3, "9");
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
     * Заполнение формы боя в модальном окне
     */
    private void fillFightFormInModal(String myFighter, String opponent, String mode, String result, String method) {
        log.info("Заполнение формы боя в модальном окне: {} vs {}, режим: {}, результат: {}, метод: {}", 
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
            methodSelect.selectByValue("EARLY_EXIT");
        } else {
            methodSelect.selectByVisibleText(method);
        }
        
        // Заполнение дополнительных полей
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("100");
        driver.findElement(By.id("rankingPosition")).sendKeys("5");
        
        // Установка даты боя (используем фиксированную дату в прошлом)
        String fightDate = "2024-01-15T20:00";
        WebElement fightDateElement = driver.findElement(By.id("fightDate"));
        fightDateElement.clear();
        fightDateElement.sendKeys(fightDate);
        
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

    /**
     * Создание тестового боя через модальное окно
     */
    private void createTestFightInModal() {
        log.info("Создание тестового боя через модальное окно");
        
        navigateTo("/fights");
        waitForPageLoad();
        
        // Клик по кнопке "Добавить бой" для открытия модального окна
        WebElement addFightButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-bs-target='#newFightModal']")
        ));
        addFightButton.click();
        
        // Ждем, пока модальное окно откроется и форма загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newFightFormContainer")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#newFightFormContainer form")));
        
        fillFightFormInModal("Тестовый боец", "Тестовый соперник", "ММА", "Победа", "Нокаут");
        
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#newFightFormContainer button[type='submit']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Ждем, пока модальное окно закроется
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("newFightModal")));
    }
}
