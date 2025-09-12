package com.ufcstats.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Простой тест для отладки создания боя
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Простой тест создания боя")
public class SimpleFightCreationTest extends BaseUITest {

    private static final Logger log = LoggerFactory.getLogger(SimpleFightCreationTest.class);

    @Test
    @DisplayName("Простое создание боя с минимальными данными")
    void testSimpleFightCreation() {
        log.info("Простой тест: Создание боя с минимальными данными");
        
        // Переход на страницу создания боя
        navigateTo("/fights/new");
        waitForPageLoad();
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("Новый бой");
        
        // Заполнение только обязательных полей
        driver.findElement(By.id("myFighter")).sendKeys("Тестовый боец");
        driver.findElement(By.id("opponent")).sendKeys("Тестовый соперник");
        
        // Установка режима боя
        WebElement modeElement = driver.findElement(By.id("mode"));
        Select modeSelect = new Select(modeElement);
        modeSelect.selectByValue("MMA");
        
        // Установка результата
        WebElement resultElement = driver.findElement(By.id("result"));
        Select resultSelect = new Select(resultElement);
        resultSelect.selectByValue("WIN");
        
        // Установка метода
        WebElement methodElement = driver.findElement(By.id("method"));
        Select methodSelect = new Select(methodElement);
        methodSelect.selectByValue("KNOCKOUT");
        
        // Заполнение минимальных числовых полей
        driver.findElement(By.id("season")).sendKeys("1");
        driver.findElement(By.id("ratingPoints")).sendKeys("100");
        driver.findElement(By.id("rankingPosition")).sendKeys("5");
        
        // Установка весовой категории
        WebElement weightClassElement = driver.findElement(By.id("weightClass"));
        Select weightClassSelect = new Select(weightClassElement);
        weightClassSelect.selectByValue("MIDDLEWEIGHT");
        
        // Установка количества раундов (используем 1 для простоты)
        WebElement roundsPlayedElement = driver.findElement(By.id("roundsPlayed"));
        roundsPlayedElement.clear();
        roundsPlayedElement.sendKeys("1");
        
        // Ждем, пока JavaScript обновит поля
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Проверим, есть ли поле даты и попробуем его заполнить
        try {
            WebElement dateField = driver.findElement(By.id("fightDate"));
            String currentDateValue = dateField.getAttribute("value");
            log.info("Текущее значение поля даты: '{}'", currentDateValue);
            
            if (currentDateValue == null || currentDateValue.trim().isEmpty()) {
                log.info("Поле даты пустое, заполняем его");
                dateField.sendKeys("2024-01-15T20:00");
            }
        } catch (Exception e) {
            log.warn("Не удалось найти или заполнить поле даты: {}", e.getMessage());
        }
        
        log.info("Все поля заполнены, отправка формы...");
        
        // Отправка формы
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        
        // Ждем и проверяем результат
        try {
            Thread.sleep(2000); // Даем время для обработки
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        
        log.info("Текущий URL после отправки формы: {}", currentUrl);
        log.info("Заголовок страницы: {}", driver.getTitle());
        
        // Проверяем, есть ли ошибки на странице
        if (pageSource.contains("error") || pageSource.contains("ошибка") || pageSource.contains("Error") || 
            pageSource.contains("invalid") || pageSource.contains("required") || pageSource.contains("валидации")) {
            log.error("Обнаружена ошибка валидации на странице");
            
            // Попробуем найти конкретные сообщения об ошибках
            if (pageSource.contains("invalid-feedback")) {
                log.error("Найдены сообщения об ошибках валидации");
            }
            
            // Выведем только часть страницы с формой для отладки
            int formStart = pageSource.indexOf("<form");
            int formEnd = pageSource.indexOf("</form>") + 7;
            if (formStart != -1 && formEnd != -1 && formEnd > formStart) {
                String formHtml = pageSource.substring(formStart, formEnd);
                log.error("HTML формы: {}", formHtml);
            }
        } else {
            log.info("Ошибок валидации на странице не обнаружено");
        }
        
        // Проверяем, куда мы попали
        if (currentUrl.contains("/fights") && !currentUrl.contains("/fights/new")) {
            log.info("✓ Успешно перенаправлены на список боев");
        } else {
            log.error("✗ Не удалось создать бой. Остались на странице: {}", currentUrl);
        }
    }
}
